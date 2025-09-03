import { useState, useEffect } from "react";
import { useNavigate, Link, useLocation } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Code, Mail, Lock, AlertCircle } from "lucide-react";
import { useToast } from "@/hooks/use-toast";
import { AuthService, LoginRequest } from "@/services/authService";

const Login = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { toast } = useToast();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [loading, setLoading] = useState(false);

  // Check for success message from location state (e.g., after signup)
  useEffect(() => {
    const state = location.state as { message?: string };
    if (state?.message) {
      toast({
        title: "Success!",
        description: state.message,
      });
      // Clear the state to prevent showing the message again
      navigate(location.pathname, { replace: true });
    }
  }, [location.pathname, location.state, navigate, toast]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const loginData: LoginRequest = {
        email: formData.email,
        password: formData.password,
      };

      const response = await AuthService.login(loginData);

      if (response.success && response.data) {
        // Get user profile after successful login
        try {
          await AuthService.getUserProfile(response.data.userId);
        } catch (profileError) {
          console.warn("Failed to fetch profile:", profileError);
        }

        toast({
          title: "Welcome back!",
          description: "You have been logged in successfully.",
        });

        // Force a page refresh to update the AuthContext
        window.location.href = "/";
      }
    } catch (error: any) {
      toast({
        title: "Login failed",
        description: error.message || "Invalid email or password.",
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-4">
      {/* Animated Background */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute -top-40 -right-40 w-80 h-80 bg-primary/10 rounded-full blur-3xl animate-pulse-glow"></div>
        <div
          className="absolute -bottom-40 -left-40 w-80 h-80 bg-accent/10 rounded-full blur-3xl animate-pulse-glow"
          style={{ animationDelay: "1s" }}
        ></div>
      </div>

      <div className="w-full max-w-md relative z-10">
        {/* Header */}
        <div className="text-center mb-8 animate-fade-in">
          <div className="flex justify-center mb-4">
            <div className="p-3 bg-gradient-primary rounded-xl animate-pulse-glow">
              <Code className="h-8 w-8 text-primary-foreground" />
            </div>
          </div>
          <h1 className="text-3xl font-bold bg-gradient-primary bg-clip-text text-transparent">
            Welcome Back
          </h1>
          <p className="text-muted-foreground mt-2">
            Sign in to your CodeCollab account
          </p>
        </div>

        {/* Login Card */}
        <Card className="bg-gradient-card border-border/50 shadow-card animate-slide-up">
          <CardHeader className="text-center">
            <CardTitle>Sign In</CardTitle>
            <CardDescription>
              Enter your credentials to access your account
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-6">
              <div className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="email">Email</Label>
                  <div className="relative">
                    <Mail className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                    <Input
                      id="email"
                      name="email"
                      type="email"
                      placeholder="Enter your email"
                      value={formData.email}
                      onChange={handleChange}
                      className="pl-10"
                      required
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="password">Password</Label>
                  <div className="relative">
                    <Lock className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                    <Input
                      id="password"
                      name="password"
                      type="password"
                      placeholder="Enter your password"
                      value={formData.password}
                      onChange={handleChange}
                      className="pl-10"
                      required
                    />
                  </div>
                </div>
              </div>

              <Button
                type="submit"
                className="w-full bg-gradient-primary hover:opacity-90 transition-opacity"
                disabled={loading}
              >
                {loading ? "Signing In..." : "Sign In"}
              </Button>
            </form>

            <div className="mt-6 text-center">
              <p className="text-muted-foreground">
                Don't have an account?{" "}
                <Link
                  to="/signup"
                  className="text-primary hover:underline font-medium"
                >
                  Sign up here
                </Link>
              </p>
            </div>
          </CardContent>
        </Card>

        {/* Back to Landing */}
        <div className="text-center mt-6">
          <Button
            variant="ghost"
            onClick={() => navigate("/")}
            className="text-muted-foreground hover:text-foreground"
          >
            ← Back to Home
          </Button>
        </div>
      </div>
    </div>
  );
};

export default Login;
