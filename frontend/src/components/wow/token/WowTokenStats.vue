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
              <td class="wow-token-stats__table-cell--min">
                {{ stats.min?.toLocaleString() ?? "-" }}
              </td>
              <td class="wow-token-stats__table-cell--max">
                {{ stats.max?.toLocaleString() ?? "-" }}
              </td>
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

.wow-token-stats__item {
  background-color: var(--color-background-surface);
  border-radius: var(--border-radius-md);
  padding: 1rem;
  display: flex;
  flex-direction: column;
  text-align: center;
}

.wow-token-stats__region-name {
  font-size: var(--font-size-2xl);
  font-weight: bold;
}

.wow-token-stats__table {
  width: 100%;
  text-align: center;
}

.wow-token-stats__table th {
  font-size: var(--font-size-lg);
  font-weight: 600;
  padding: 0.5rem 0;
  letter-spacing: 0.05em;
}

.wow-token-stats__table td {
  padding: 0.5rem 0;
  background-color: #1f1f25;
  border-radius: var(--border-radius-md);
}

.wow-token-stats__table-cell--min {
  color: var(--color-price-negative);
}

.wow-token-stats__table-cell--max {
  color: var(--color-price-positive);
}

@media (min-width: 768px) {
  .wow-token-stats {
    grid-template-columns: repeat(2, 1fr);
  }

  .wow-token-stats__region-name {
    font-size: var(--font-size-3xl);
  }
}

@media (min-width: 1024px) {
  .wow-token-stats {
    grid-template-columns: repeat(4, 1fr);
  }
}
</style>
