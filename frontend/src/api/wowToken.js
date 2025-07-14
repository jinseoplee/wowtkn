import apiClient from "./apiClient";

export const fetchWowTokens = async () => {
  const response = await apiClient.get("/wow-tokens");
  return response.data;
};
