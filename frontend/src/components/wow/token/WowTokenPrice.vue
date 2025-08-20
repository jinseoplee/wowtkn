<script setup>
const props = defineProps({
  currentPriceByRegion: {
    type: Object,
    required: true,
  },
});

const formatChange = (data) => {
  if (!data?.changeAmount && !data?.changeRate) return "-";

  const { changeAmount, changeRate } = data;
  const arrow = changeAmount > 0 ? "▲" : "▼";
  const rateSign = changeRate > 0 ? "+" : "";

  return `${rateSign}${changeRate.toFixed(2)}% ${arrow} ${Math.abs(
    changeAmount
  ).toLocaleString()}`;
};
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
          {{ data?.currentPrice?.toLocaleString() ?? "-" }}
        </p>
        <p
          class="wow-token-prices__change"
          :class="{
            'wow-token-prices__change--positive': data.changeAmount > 0,
            'wow-token-prices__change--negative': data.changeAmount < 0,
          }"
        >
          {{ formatChange(data) }}
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

.wow-token-prices__item {
  background-color: var(--color-background-surface);
  border-radius: var(--border-radius-md);
  text-align: center;
  padding: 1rem;
}

.wow-token-prices__region-name {
  font-size: var(--font-size-2xl);
  font-weight: bold;
}

.wow-token-prices__current-price {
  font-size: var(--font-size-3xl);
  font-weight: 800;
}

.wow-token-prices__current-price--positive,
.wow-token-prices__change--positive {
  color: var(--color-price-positive);
}

.wow-token-prices__current-price--negative,
.wow-token-prices__change--negative {
  color: var(--color-price-negative);
}

@media (min-width: 768px) {
  .wow-token-prices {
    grid-template-columns: repeat(2, 1fr);
  }
  .wow-token-prices__region-name {
    font-size: var(--font-size-3xl);
  }
  .wow-token-prices__current-price {
    font-size: var(--font-size-4xl);
  }
}
@media (min-width: 1024px) {
  .wow-token-prices {
    grid-template-columns: repeat(4, 1fr);
  }
}
</style>
