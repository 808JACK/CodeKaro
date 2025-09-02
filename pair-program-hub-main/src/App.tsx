import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./contexts/AuthContext";
import Landing from "./pages/Landing";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import OtpVerification from "./pages/OtpVerification";
import Dashboard from "./pages/Dashboard";
import CreateRoom from "./pages/CreateRoom";
import CreateContest from "./pages/CreateContest";
import SelectProblems from "./pages/SelectProblems";
import Room from "./pages/Room";
import ContestRoom from "./pages/ContestRoom";
import NotFound from "./pages/NotFound";

const queryClient = new QueryClient();

// Protected route component
const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />;
};

// Public route component (redirects authenticated users to dashboard)
const PublicRoute = ({ children }: { children: React.ReactNode }) => {
  const { isAuthenticated } = useAuth();
  return !isAuthenticated ? <>{children}</> : <Navigate to="/" replace />;
};

const AppRoutes = () => {
  const { isAuthenticated } = useAuth();

  // Keep-alive is handled by server-side service - no need for frontend pings
  // useEffect(() => {
  //   if (isAuthenticated) {
  //     keepAliveService.start();
  //   } else {
  //     keepAliveService.stop();
  //   }
  //   
  //   return () => {
  //     keepAliveService.stop();
  //   };
  // }, [isAuthenticated]);

  return (
    <Routes>
      {/* Default route - shows landing for guests, dashboard for authenticated users */}
      <Route path="/" element={isAuthenticated ? <Dashboard /> : <Navigate to="/landing" replace />} />
      
      {/* Public routes (only for non-authenticated users) */}
      <Route path="/landing" element={<PublicRoute><Landing /></PublicRoute>} />
      <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
      <Route path="/signup" element={<PublicRoute><Signup /></PublicRoute>} />
      <Route path="/otp-verification" element={<PublicRoute><OtpVerification /></PublicRoute>} />
      
      {/* Protected routes (only for authenticated users) */}
      <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
      <Route path="/create-room" element={<ProtectedRoute><CreateRoom /></ProtectedRoute>} />
      <Route path="/create-contest" element={<ProtectedRoute><CreateContest /></ProtectedRoute>} />
      <Route path="/select-problems/:roomCode" element={<ProtectedRoute><SelectProblems /></ProtectedRoute>} />
      <Route path="/room/:roomCode" element={<ProtectedRoute><Room /></ProtectedRoute>} />
      <Route path="/contest/:contestId" element={<ProtectedRoute><ContestRoom /></ProtectedRoute>} />
      
      {/* Catch-all route */}
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

const App = () => (
  <QueryClientProvider client={queryClient}>
    <AuthProvider>
      <TooltipProvider>
        <Toaster />
        <Sonner />
        <BrowserRouter>
          <AppRoutes />
        </BrowserRouter>
      </TooltipProvider>
    </AuthProvider>
  </QueryClientProvider>
);

export default App;
