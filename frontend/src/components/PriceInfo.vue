<script setup>
import { ref, computed, onMounted, onUnmounted } from "vue";
import { fetchWowTokens } from "@/api/wowToken";
import PriceItem from "./PriceItem.vue";

import {
  getUpdatedAgo,
  formatNumber,
  formatSignNumber,
  formatChangeRate,
} from "@/utils/formatter";

const REGION_ORDER = ["US", "EU", "KR", "TW"];
const REFRESH_INTERVAL_MS =
  Number(import.meta.env.VITE_REFRESH_INTERVAL_MS) || 300000;

const rawTokens = ref([]);
const isLoading = ref(false);
const hasError = ref(false);
const errorMessage = ref("");
let timeoutId = null;

const loadTokens = async () => {
  isLoading.value = true;
  try {
    hasError.value = false;
    errorMessage.value = "";
    const data = await fetchWowTokens();
    rawTokens.value = data;
  } catch (error) {
    hasError.value = true;
    errorMessage.value = "Failed to load data. Please try again later.";
  } finally {
    isLoading.value = false;
    timeoutId = setTimeout(loadTokens, REFRESH_INTERVAL_MS);
  }
};

onMounted(() => {
  loadTokens();
});

onUnmounted(() => {
  clearTimeout(timeoutId);
});

const tokens = computed(() => {
  return rawTokens.value
    .map((token) => ({
      region: token.region,
      price: formatNumber(token.price),
      formattedChangeAmount: formatSignNumber(token.changeAmount),
      changeAmount: token.changeAmount,
      changeRate: formatChangeRate(token.changeRate),
      updatedAgo: getUpdatedAgo(token.timestamp),
    }))
    .sort((a, b) => {
      const indexA = REGION_ORDER.indexOf(a.region);
      const indexB = REGION_ORDER.indexOf(b.region);
      return indexA - indexB;
    });
});
</script>

<template>
  <section class="price-info-section">
    <div v-if="isLoading" class="message-box message--loading">
      Loading WoW Token Prices...
    </div>
    <div v-else-if="hasError" class="message-box message--error">
      {{ errorMessage }}
    </div>
    <div v-else class="price-list">
      <PriceItem v-for="token in tokens" :key="token.region" :token="token" />
    </div>
  </section>
</template>

<style scoped>
.price-info-section {
  margin-bottom: var(--spacing-md);
}

.price-list {
  display: grid;
  gap: var(--spacing-md);
  grid-template-columns: repeat(1, 1fr);
}

@media (min-width: 481px) {
  .price-list {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 992px) {
  .price-list {
    grid-template-columns: repeat(4, 1fr);
  }
}
</style>
