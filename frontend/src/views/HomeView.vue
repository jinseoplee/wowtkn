<script setup>
import { useWowTokens } from "@/composables/useWowTokens";
import WowTokenPrice from "@/components/wow/token/WowTokenPrice.vue";
import WowTokenChart from "@/components/wow/token/WowTokenChart.vue";
import WowTokenStats from "@/components/wow/token/WowTokenStats.vue";

const regions = ["US", "EU", "KR", "TW"];
const fetchDays = 7;

const { wowTokensByRegion, currentPriceByRegion, wowTokenStats } = useWowTokens(
  regions,
  fetchDays
);
</script>

<template>
  <div class="home-view">
    <WowTokenPrice :current-price-by-region="currentPriceByRegion" />
    <WowTokenChart :wow-tokens-by-region="wowTokensByRegion" />
    <WowTokenStats :wow-token-stats="wowTokenStats" />
  </div>
</template>

<style scoped>
.home-view {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
</style>
