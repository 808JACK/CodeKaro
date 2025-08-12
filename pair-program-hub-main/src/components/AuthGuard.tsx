import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthService } from '@/services/authService';

interface AuthGuardProps {
  children: React.ReactNode;
  requireAuth?: boolean;
}

const AuthGuard: React.FC<AuthGuardProps> = ({ children, requireAuth = true }) => {
  const navigate = useNavigate();

  useEffect(() => {
    const isAuthenticated = AuthService.isAuthenticated();
    
    if (requireAuth && !isAuthenticated) {
      navigate('/login');
    } else if (!requireAuth && isAuthenticated) {
      navigate('/');
    }
  }, [requireAuth, navigate]);

  return <>{children}</>;
};

export default AuthGuard;
