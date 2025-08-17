import { ref, computed, onMounted } from "vue";
import { getWowTokens } from "@/api/wowToken";
import { getCurrentPrice } from "@/utils/price";
import { getPriceStatsByPeriod } from "@/utils/stats";

export function useWowTokens(
  regions = ["US", "EU", "KR", "TW"],
  fetchDays = 7
) {
  const wowTokensByRegion = ref({});

  const fetchWowTokens = async () => {
    try {
      const responses = await Promise.all(
        regions.map((region) => getWowTokens(region, fetchDays))
      );

      wowTokensByRegion.value = Object.fromEntries(
        responses.map((response, idx) => [regions[idx], response.data])
      );
    } catch (err) {
      console.error("Failed to fetch WoW tokens");
    }
  };

  const currentPriceByRegion = computed(() =>
    Object.fromEntries(
      Object.entries(wowTokensByRegion.value).map(([region, wowTokens]) => [
        region,
        getCurrentPrice(wowTokens),
      ])
    )
  );

  const wowTokenStats = computed(() =>
    Object.fromEntries(
      Object.entries(wowTokensByRegion.value).map(([region, wowTokens]) => [
        region,
        getPriceStatsByPeriod(wowTokens),
      ])
    )
  );

  onMounted(fetchWowTokens);

  return { wowTokensByRegion, currentPriceByRegion, wowTokenStats };
}
