<script setup>
const props = defineProps({
  wowTokenStats: {
    type: Object,
    required: true,
  },
});
</script>

<template>
  <div class="wow-token-stats">
    <div
      v-for="(periodStats, region) in wowTokenStats"
      :key="region"
      class="wow-token-stats__item"
    >
      <div class="wow-token-stats__item-header">
        <span class="wow-token-stats__region-name">{{ region }}</span>
      </div>

      <div class="wow-token-stats__item-body">
        <table class="wow-token-stats__table">
          <thead>
            <tr>
              <th scope="col">Period</th>
              <th scope="col">Min</th>
              <th scope="col">Max</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(stats, period) in periodStats" :key="period">
              <td>{{ period }}</td>
              <td>{{ stats.min?.toLocaleString() ?? "-" }}</td>
              <td>{{ stats.max?.toLocaleString() ?? "-" }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style scoped>
.wow-token-stats {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 1rem;
}

@media (min-width: 768px) {
  .wow-token-stats {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .wow-token-stats {
    grid-template-columns: repeat(4, 1fr);
  }
}

.wow-token-stats__item {
  background-color: var(--color-background-surface);
  padding: 1rem;
  display: flex;
  flex-direction: column;
  align-items: stretch;
}

.wow-token-stats__item-header {
  text-align: center;
  margin-bottom: 0.5rem;
}

.wow-token-stats__region-name {
  font-size: var(--font-size-lg);
  font-weight: bold;
}

@media (min-width: 768px) {
  .wow-token-stats__region-name {
    font-size: var(--font-size-xl);
  }
}

.wow-token-stats__table {
  width: 100%;
  border-collapse: collapse;
  text-align: center;
  table-layout: fixed;
}

.wow-token-stats__table th {
  padding: 0.5rem 0;
}

.wow-token-stats__table td {
  padding: 0.5rem 0;
  border-top: 1px solid #2a2c33;
}

.wow-token-stats__table td:nth-child(2) {
  color: var(--color-price-negative);
}

.wow-token-stats__table td:nth-child(3) {
  color: var(--color-price-positive);
}
</style>
