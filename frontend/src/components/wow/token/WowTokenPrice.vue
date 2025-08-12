<script setup>
const props = defineProps({
  currentPriceByRegion: {
    type: Object,
    required: true,
  },
});
</script>

<template>
  <div class="wow-token-prices">
    <div
      v-for="(data, region) in currentPriceByRegion"
      :key="region"
      class="wow-token-prices__item"
    >
      <div class="wow-token-prices__item-header">
        <span class="wow-token-prices__region-name">{{ region }}</span>
      </div>
      <div class="wow-token-prices__item-body">
        <p
          class="wow-token-prices__current-price"
          :class="{
            'wow-token-prices__current-price--positive': data.changeAmount > 0,
            'wow-token-prices__current-price--negative': data.changeAmount < 0,
          }"
        >
          {{ data.currentPrice.toLocaleString() }}
        </p>
        <p
          class="wow-token-prices__change"
          :class="{
            'wow-token-prices__change--positive': data.changeAmount > 0,
            'wow-token-prices__change--negative': data.changeAmount < 0,
          }"
        >
          {{ data.changeAmount.toLocaleString() }}
          ({{ data.changeRate.toFixed(2) }}%)
        </p>
        <p class="wow-token-prices__time-ago">
          {{ data.timeAgo }}
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.wow-token-prices {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 1rem;
}

@media (min-width: 768px) {
  .wow-token-prices {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .wow-token-prices {
    grid-template-columns: repeat(4, 1fr);
  }
}

.wow-token-prices__item {
  background-color: var(--color-background-surface);
  text-align: center;
  padding: 1rem;
}

.wow-token-prices__region-name {
  font-size: var(--font-size-lg);
  font-weight: bold;
}

@media (min-width: 768px) {
  .wow-token-prices__region-name {
    font-size: var(--font-size-xl);
  }
}

.wow-token-prices__current-price {
  font-size: var(--font-size-2xl);
  font-weight: bold;
}

@media (min-width: 768px) {
  .wow-token-prices__current-price {
    font-size: var(--font-size-3xl);
  }
}

.wow-token-prices__time-ago {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
}

.wow-token-prices__current-price--positive,
.wow-token-prices__change--positive {
  color: var(--color-price-positive);
}

.wow-token-prices__current-price--negative,
.wow-token-prices__change--negative {
  color: var(--color-price-negative);
}
</style>
