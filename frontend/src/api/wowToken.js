import apiClient from "./apiClient";

const ONE_DAY_MS = 24 * 60 * 60 * 1000;
const SEVEN_DAYS_MS = 7 * ONE_DAY_MS;

export const fetchWowTokens = async () => {
  const response = await apiClient.get("/wow-tokens");
  return response.data;
};

export const fetchWowTokenPriceHistory = async (region) => {
  const endTime = Date.now();
  const startTime = endTime - SEVEN_DAYS_MS;

  const response = await apiClient.get("/wow-tokens/history", {
    params: { region, startTime, endTime },
  });

  return response.data;
};
