import React, { createContext, useContext, useState } from 'react';

// Create a context for managing authentication state
const AuthContext = createContext();

/**
 * AuthProvider component that provides authentication state and functions
 * to its children via React Context.
 * 
 * @param {object} children - React components that will consume the context.
 */
export const AuthProvider = ({ children }) => {
  // State to track if the user is authenticated
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  /**
   * Login function to set the user as authenticated.
   */
  const login = () => setIsAuthenticated(true);

  /**
   * Logout function to set the user as not authenticated.
   */
  const logout = () => setIsAuthenticated(false);

  /**
   * Provide the authentication state and methods to the consuming components.
   */
  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

/**
 * Custom hook to access the authentication context.
 * 
 * @returns {object} - The context value containing `isAuthenticated`, `login`, and `logout`.
 */
export const useAuth = () => useContext(AuthContext);
