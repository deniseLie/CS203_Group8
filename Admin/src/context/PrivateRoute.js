import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from './AuthContext';  // Import the useAuth hook

const PrivateRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();  // Get the auth status from the context

  if (!isAuthenticated) {
    return <Navigate to="/" replace />;  // Redirect to login page if not authenticated
  }

  return children;  // Render the protected route if authenticated
};

export default PrivateRoute;
