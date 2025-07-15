<script setup>
import { ref, computed, onMounted, onBeforeUnmount, watch } from "vue";
import { fetchWowTokenPriceHistory } from "@/api/wowToken";
import { formatDateTime } from "@/utils/formatter";
import { LineChart } from "vue-chart-3";

import {
  BREAKPOINTS,
  REGIONS,
  TIME_RANGES,
  CHART_COLORS,
} from "@/constants/chartConstants";

import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
  LineController,
} from "chart.js";

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
  LineController
);

const windowWidth = ref(window.innerWidth);
const selectedRegion = ref(REGIONS[0]);
const selectedHours = ref(TIME_RANGES[1].value);
const chartData = ref({
  labels: [],
  datasets: [],
});
const userTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;

const isLoading = ref(true);
const hasError = ref(false);
const errorMessage = ref("");

const onResize = () => {
  windowWidth.value = window.innerWidth;
};

const chartOptions = computed(() => {
  let maxTicksLimit;
  if (windowWidth.value < BREAKPOINTS.MOBILE) {
    maxTicksLimit = 3;
  } else if (windowWidth.value < BREAKPOINTS.TABLET) {
    maxTicksLimit = 5;
  } else {
    maxTicksLimit = 8;
  }

  return {
    responsive: true,
    maintainAspectRatio: false,
    interaction: {
      mode: "index",
      intersect: false,
    },
    plugins: {
      legend: { display: false },
      tooltip: {
        backgroundColor: CHART_COLORS.TOOLTIP_BG,
        titleColor: CHART_COLORS.TOOLTIP_TITLE,
        bodyColor: CHART_COLORS.TOOLTIP_BODY,
        borderColor: CHART_COLORS.TOOLTIP_BORDER,
        borderWidth: 1,
        displayColors: false,
      },
    },
    scales: {
      x: {
        grid: { color: CHART_COLORS.GRID_LINES },
        ticks: {
          color: CHART_COLORS.AXIS_TICKS,
          autoSkip: true,
          maxTicksLimit: maxTicksLimit,
          maxRotation: 0,
          minRotation: 0,
          padding: 10,
        },
      },
      y: {
        grid: { color: CHART_COLORS.GRID_LINES },
        ticks: { color: CHART_COLORS.AXIS_TICKS },
      },
    },
  };
});

const updateChartData = async () => {
  isLoading.value = true;
  hasError.value = false;
  errorMessage.value = "";

  try {
    const rawData = await fetchWowTokenPriceHistory(selectedRegion.value);

    const currentTime = Date.now();
    const filteredData = rawData.filter((record) => {
      return (
        currentTime - record.timestamp <= selectedHours.value * 60 * 60 * 1000
      );
    });

    const labels = filteredData.map((record) =>
      formatDateTime(record.timestamp, userTimeZone)
    );
    const prices = filteredData.map((record) => record.price);

    chartData.value = {
      labels,
      datasets: [
        {
          data: prices,
          borderColor: CHART_COLORS.BORDER,
          backgroundColor: CHART_COLORS.BACKGROUND,
          tension: 0.4,
          fill: true,
          pointRadius: 0,
          pointHoverRadius: 5,
          pointHoverBackgroundColor: CHART_COLORS.BORDER,
          pointHoverBorderColor: "#ffffff",
          borderWidth: 2,
        },
      ],
    };
  } catch (error) {
    hasError.value = true;
    errorMessage.value = "Failed to load data. Please try again later.";
    chartData.value = { labels: [], datasets: [] };
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  updateChartData();
  window.addEventListener("resize", onResize);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", onResize);
});

watch([selectedRegion, selectedHours], () => {
  updateChartData();
});
</script>

<template>
  <section class="price-chart-section">
    <div v-if="isLoading" class="message-box message--loading">
      Loading WoW Token Prices...
    </div>
    <div v-else-if="hasError" class="message-box message--error">
      {{ errorMessage }}
    </div>
    <div v-else class="chart-container">
      <h2 class="price-chart__title">WoW Token Price Chart</h2>

      <div class="price-chart-control">
        <div class="region-selector">
          <button
            v-for="region in REGIONS"
            :key="region"
            :class="{ active: selectedRegion === region }"
            @click="selectedRegion = region"
          >
            {{ region }}
          </button>
        </div>

        <div class="btn-group">
          <button
            v-for="range in TIME_RANGES"
            :key="range.value"
            :class="{ active: selectedHours === range.value }"
            @click="selectedHours = range.value"
          >
            {{ range.label }}
          </button>
        </div>
      </div>
      <LineChart :chart-data="chartData" :options="chartOptions" />
    </div>
  </section>
</template>

<style scoped>
.price-chart-section {
  background-color: var(--background-dark-secondary);
  border-radius: var(--border-radius-md);
  padding: var(--spacing-md);
}

.price-chart__title {
  margin-bottom: var(--spacing-md);
  font-size: 1.3em;
  font-weight: bold;
  text-align: center;
}

.price-chart-control {
  display: flex;
  justify-content: space-between;
  margin-bottom: var(--spacing-md);
}

.region-selector,
.btn-group {
  display: flex;
  gap: var(--spacing-xs);
}

.region-selector button,
.btn-group button {
  background-color: transparent;
  border: none;
  color: var(--color-text-light);
  padding: var(--spacing-xs);
  font-size: 1em;
  cursor: pointer;
}

.region-selector button:hover,
.btn-group button:hover {
  color: #00cfff;
}

.region-selector button.active {
  color: #00cfff;
}

.btn-group button.active {
  color: #00cfff;
}
</style>
