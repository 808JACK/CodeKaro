export async function fetchWithAuth(url: string, options: RequestInit = {}) {
  const token = localStorage.getItem("accessToken");

  console.log("🔐 fetchWithAuth called:", {
    url,
    hasToken: !!token,
    tokenPreview: token ? `${token.substring(0, 20)}...` : 'none',
    allStorageKeys: {
      accessToken: !!localStorage.getItem("accessToken"),
      refreshToken: !!localStorage.getItem("refreshToken"),
      userId: localStorage.getItem("userId"),
      userInfo: !!localStorage.getItem("userInfo")
    }
  });

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

  console.log("📡 Response received:", {
    status: response.status,
    ok: response.ok,
    headers: Object.fromEntries(response.headers.entries())
  });

  // Update access token if Gateway returns a new one
  const newToken = response.headers.get("X-New-Access-Token");
  if (newToken) {
    localStorage.setItem("accessToken", newToken);
    console.log("🔄 Token refreshed automatically");
  }

  // Check if refresh token expired
  const refreshExpired = response.headers.get("X-Refresh-Expired");
  if (refreshExpired === "true") {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken"); // if stored
    console.log("🚪 Refresh token expired, redirecting to login");
    window.location.href = "http://localhost:4000/login";
    return; // stop execution
  }

  // Parse response data
  let data: unknown = null;
  const contentType = response.headers.get("Content-Type") || "";
  
  try {
    if (contentType.includes("application/json")) {
      data = await response.json();
    } else {
      // try text for empty bodies
      const text = await response.text();
      data = text ? { message: text } : {};
    }
  } catch (parseError) {
    console.error("Failed to parse response:", parseError);
    data = { message: "Failed to parse response" };
  }

  // Handle 401 Unauthorized specifically
  if (response.status === 401) {
    console.error("🚫 401 Unauthorized - Authentication failed");
    console.error("Token info:", {
      hasToken: !!token,
      refreshExpired: refreshExpired,
      newToken: !!newToken
    });
    
    // If no refresh token expired header, this might be a token validation issue
    if (!refreshExpired) {
      console.error("🔍 Token validation failed - clearing tokens and redirecting to login");
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      window.location.href = "http://localhost:4000/login";
      return;
    }
  }

  // Check if request failed
  if (!response.ok) {
    const errorMessage = (data && typeof data === 'object' && data.message) 
      ? data.message 
      : `Request failed with status ${response.status}`;
    console.error("❌ Request failed:", errorMessage);
    throw new Error(errorMessage);
  }

  return data;
}

