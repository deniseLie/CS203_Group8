import React from 'react';
import { Navigate } from 'react-router-dom'; // Navigate component for redirection
import { useAuth } from './AuthContext'; // Import the custom hook to access authentication context

/**
 * PrivateRoute component to protect routes and allow access only to authenticated users.
 *
 * @param {object} children - The child components to render if the user is authenticated.
 * @returns {JSX.Element} - The child components or a redirection to the login page.
 */
const PrivateRoute = ({ children }) => {
  const { isAuthenticated } = useAuth(); // Access authentication status from the context

  /**
   * If the user is not authenticated, redirect to the login page.
   * The `replace` prop ensures the browser history is updated to avoid "back" navigation to the protected route.
   */
  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  /**
   * If the user is authenticated, render the child components.
   */
  return children;
};

export default PrivateRoute;
