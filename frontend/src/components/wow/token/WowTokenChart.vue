<script setup>
import { computed, onMounted, onUnmounted, ref } from "vue";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Line } from "vue-chartjs";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const props = defineProps({
  wowTokensByRegion: {
    type: Object,
    required: true,
    default: () => ({}),
  },
});

const days = [
  { value: 1, label: "1d" },
  { value: 7, label: "1w" },
  { value: 30, label: "1m" },
];

const selectedRegion = ref("US");
const selectedDay = ref(1);
const maxTicksLimitX = ref(2);

const selectRegion = (region) => {
  selectedRegion.value = region;
};

const selectDay = (day) => {
  selectedDay.value = day;
};

const updateMaxTicksLimit = () => {
  const width = window.innerWidth;
  if (width >= 1024) {
    maxTicksLimitX.value = 6;
  } else if (width >= 768) {
    maxTicksLimitX.value = 4;
  } else {
    maxTicksLimitX.value = 2;
  }
};

onMounted(() => {
  updateMaxTicksLimit();
  window.addEventListener("resize", updateMaxTicksLimit);
});

onUnmounted(() => {
  window.removeEventListener("resize", updateMaxTicksLimit);
});

const chartData = computed(() => {
  const wowTokens = props.wowTokensByRegion[selectedRegion.value] || [];
  const now = Date.now();
  const startTime = now - selectedDay.value * (24 * 60 * 60 * 1000);

  const filtered = wowTokens.filter(
    (wowToken) => wowToken.timestamp >= startTime
  );

  return {
    labels: filtered.map((item) =>
      new Date(item.timestamp).toLocaleString(undefined, {
        hour12: false,
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
      })
    ),
    datasets: [
      {
        data: filtered.map((item) => item.price),
        borderColor: "#00cfff",
        backgroundColor: "#00cfff",
        tension: 0.3,
      },
    ],
  };
});

const chartOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      mode: "nearest",
      intersect: false,
      displayColors: false,
    },
  },
  scales: {
    x: {
      ticks: {
        maxTicksLimit: maxTicksLimitX.value,
      },
    },
  },
}));
</script>

<template>
  <div class="wow-token-chart">
    <h2 class="wow-token-chart__title">WoW Token price chart</h2>
    <div class="wow-token-chart__control">
      <div class="wow-token-chart__button-group">
        <button
          v-for="(_, region) in wowTokensByRegion || []"
          :key="region"
          @click="selectRegion(region)"
          class="wow-token-chart__button"
          :class="{
            'wow-token-chart__button--active': selectedRegion === region,
          }"
        >
          {{ region }}
        </button>
      </div>

      <div class="wow-token-chart__button-group">
        <button
          v-for="dayOption in days"
          :key="dayOption.value"
          @click="selectDay(dayOption.value)"
          class="wow-token-chart__button"
          :class="{
            'wow-token-chart__button--active': selectedDay === dayOption.value,
          }"
        >
          {{ dayOption.label }}
        </button>
      </div>
    </div>

    <div class="wow-token-chart__chart-wrapper">
      <Line :data="chartData" :options="chartOptions" />
    </div>
  </div>
</template>

<style scoped>
.wow-token-chart {
  background-color: var(--color-background-surface);
  border-radius: var(--border-radius-md);
  padding: 1rem;
}

.wow-token-chart__title {
  text-align: center;
  font-size: var(--font-size-lg);
  margin-bottom: 1rem;
}

@media (min-width: 768px) {
  .wow-token-chart__title {
    font-size: var(--font-size-xl);
  }
}

.wow-token-chart__control {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.wow-token-chart__button-group {
  display: flex;
  gap: 4px;
}

.wow-token-chart__button {
  background-color: transparent;
  border: none;
  color: var(--color-text-light);
  font-size: var(--font-size);
  padding: 4px;
  cursor: pointer;
}

.wow-token-chart__button--active {
  color: #00cfff;
}

.wow-token-chart__chart-wrapper {
  width: 100%;
  height: 300px;
  touch-action: none;
}

@media (min-width: 768px) {
  .wow-token-chart__chart-wrapper {
    height: 350px;
  }
}

@media (min-width: 1024px) {
  .wow-token-chart__chart-wrapper {
    height: 450px;
  }
}
</style>
