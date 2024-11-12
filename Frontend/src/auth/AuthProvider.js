import React, { createContext, useContext, useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode"; // Correct import for jwt-decode
import Cookies from "js-cookie";

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

const isTokenExpired = (token) => {
  try {
    const decodedToken = jwtDecode(token);
    return decodedToken.exp * 1000 < Date.now();
  } catch (error) {
    console.error("Invalid token:", error);
    return true;
  }
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const token = Cookies.get("jwtToken");

    if (token && !isTokenExpired(token)) {
      setUser(jwtDecode(token));
    } else {
      Cookies.remove("jwtToken");
      setUser(null);
    }
  }, []);

  const login = (token) => {
    Cookies.set("jwtToken", token, { path: '/' });
    setUser(jwtDecode(token));
  };

  const logout = () => {
    Cookies.remove("jwtToken");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
