import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Label } from '@/components/ui/label';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { AuthService, SignupRequest } from '@/services/authService';

interface LocationState {
  signupData: SignupRequest;
  email: string;
}

const OtpVerification: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [otp, setOtp] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [countdown, setCountdown] = useState(30);

  // Get signup data from location state
  const state = location.state as LocationState;
  const signupData = state?.signupData;
  const email = state?.email;

  useEffect(() => {
    // Redirect if no signup data
    if (!signupData || !email) {
      navigate('/signup');
      return;
    }

    // Start countdown
    const timer = setInterval(() => {
      setCountdown((prev) => {
        if (prev <= 1) {
          clearInterval(timer);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(timer);
  }, [signupData, email, navigate]);

  const handleOtpChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/\D/g, '').slice(0, 6);
    setOtp(value);
  };

  const handleVerifyOtp = async () => {
    if (otp.length !== 6) {
      setError('Please enter a valid 6-digit OTP');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await AuthService.verifyOtpAndSaveUser(email, otp, signupData);
      
      if (response.success) {
        setSuccess('Account created successfully! Redirecting to login...');
        setTimeout(() => {
          navigate('/login', { 
            state: { 
              message: 'Account created successfully! Please login with your credentials.' 
            } 
          });
        }, 2000);
      }
    } catch (error: any) {
      setError(error.message || 'OTP verification failed');
    } finally {
      setLoading(false);
    }
  };

  const handleResendOtp = async () => {
    setLoading(true);
    setError('');

    try {
      await AuthService.signup(signupData);
      setSuccess('OTP resent successfully!');
      setCountdown(30);
    } catch (error: any) {
      setError(error.message || 'Failed to resend OTP');
    } finally {
      setLoading(false);
    }
  };

  if (!signupData || !email) {
    return null;
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100 p-4">
      <Card className="w-full max-w-md">
        <CardHeader className="text-center">
          <CardTitle className="text-2xl font-bold text-gray-900">
            Verify Your Email
          </CardTitle>
          <CardDescription className="text-gray-600">
            We've sent a 6-digit code to <span className="font-semibold">{email}</span>
          </CardDescription>
        </CardHeader>
        
        <CardContent className="space-y-6">
          {error && (
            <Alert variant="destructive">
              <AlertDescription>{error}</AlertDescription>
            </Alert>
          )}
          
          {success && (
            <Alert>
              <AlertDescription>{success}</AlertDescription>
            </Alert>
          )}

          <div className="space-y-2">
            <Label htmlFor="otp">Enter OTP</Label>
            <Input
              id="otp"
              type="text"
              placeholder="Enter 6-digit OTP"
              value={otp}
              onChange={handleOtpChange}
              maxLength={6}
              className="text-center text-lg tracking-widest"
              disabled={loading}
            />
          </div>

          <Button
            onClick={handleVerifyOtp}
            disabled={loading || otp.length !== 6}
            className="w-full"
          >
            {loading ? 'Verifying...' : 'Verify OTP'}
          </Button>

          <div className="text-center space-y-2">
            <p className="text-sm text-gray-600">
              Didn't receive the code? 
              {countdown > 0 ? (
                <span className="text-gray-400 ml-1">
                  Resend in {countdown}s
                </span>
              ) : (
                <button
                  onClick={handleResendOtp}
                  disabled={loading}
                  className="text-blue-600 hover:text-blue-800 ml-1 font-medium"
                >
                  Resend OTP
                </button>
              )}
            </p>
          </div>

          <div className="text-center">
            <button
              onClick={() => navigate('/signup')}
              className="text-sm text-gray-600 hover:text-gray-800"
              disabled={loading}
            >
              ← Back to Signup
            </button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default OtpVerification;