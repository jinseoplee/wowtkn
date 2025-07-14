<script setup>
const props = defineProps({
  token: {
    type: Object,
    required: true,
    validator: (value) => {
      return (
        typeof value.region === "string" &&
        typeof value.price === "string" &&
        typeof value.formattedChangeAmount === "string" &&
        typeof value.changeAmount === "number" &&
        typeof value.changeRate === "string" &&
        typeof value.updatedAgo === "string"
      );
    },
  },
});
</script>

<template>
  <div class="price-item">
    <span class="region">{{ token.region }}</span>
    <span
      :class="[
        'price',
        token.changeAmount > 0 ? 'color-price-up' : 'color-price-down',
      ]"
      >{{ token.price }}</span
    >
    <span
      :class="[
        'change-info',
        token.changeAmount > 0 ? 'color-price-up' : 'color-price-down',
      ]"
    >
      {{ token.formattedChangeAmount }} ({{ token.changeRate }})
    </span>
    <span class="updated-ago">Updated: {{ token.updatedAgo }}</span>
  </div>
</template>

<style scoped>
.price-item {
  background-color: var(--background-dark-secondary);
  padding: var(--spacing-md);
  border-radius: var(--border-radius-sm);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  width: 100%;
}

.price-item .region {
  font-size: 1.3em;
  font-weight: bold;
  margin-bottom: var(--spacing-xs);
}

.price-item .price {
  font-size: 2em;
  font-weight: bold;
}

.price-item .change-info {
  font-size: 1.3em;
  font-weight: bold;
  margin-bottom: var(--spacing-xs);
}

.color-price-up {
  color: var(--color-price-up);
}

.color-price-down {
  color: var(--color-price-down);
}

.price-item .updated-ago {
  font-size: 0.9em;
  color: var(--color-text-muted);
  margin-top: var(--spacing-xs);
}
</style>
