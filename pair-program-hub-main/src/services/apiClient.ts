export async function fetchWithAuth(url: string, options: RequestInit = {}) {
  const token = localStorage.getItem("accessToken");

  // Merge headers
  const mergedHeaders: Record<string, string> = {
    ...(options.headers as Record<string, string> || {}),
    "Content-Type": "application/json",
  };
  if (token) {
    mergedHeaders["Authorization"] = `Bearer ${token}`;
  }
  options.headers = mergedHeaders;

  const response = await fetch(url, options);

  // Update access token if Gateway returns a new one
  const newToken = response.headers.get("X-New-Access-Token");
  if (newToken) localStorage.setItem("accessToken", newToken);

  // Check if refresh token expired
  const refreshExpired = response.headers.get("X-Refresh-Expired");
  if (refreshExpired === "true") {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken"); // if stored
    window.location.href = "http://localhost:4000/login";
    return; // stop execution
  }

  let data: any = null;
  const contentType = response.headers.get("Content-Type") || "";
  if (contentType.includes("application/json")) {
    data = await response.json();
  } else {
    // try text for empty bodies
    const text = await response.text();
    data = text ? { message: text } : {};
  }

  if (!response.ok) {
    throw new Error(data.message || "Request failed");
  }

  return data;
}

