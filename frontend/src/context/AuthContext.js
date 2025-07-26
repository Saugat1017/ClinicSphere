import React, { createContext, useContext, useState, useEffect } from "react";
import { patientAPI, doctorAPI, adminAPI } from "../services/api";

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check for existing token on app load
    const savedToken = localStorage.getItem("token");
    const savedUser = localStorage.getItem("user");

    if (savedToken && savedUser) {
      setToken(savedToken);
      setUser(JSON.parse(savedUser));
    }
    setLoading(false);
  }, []);

  const login = async (credentials, userType) => {
    try {
      let response;

      switch (userType) {
        case "patient":
          response = await patientAPI.login(credentials);
          break;
        case "doctor":
          response = await doctorAPI.login(credentials);
          break;
        case "admin":
          response = await adminAPI.login(credentials);
          break;
        default:
          throw new Error("Invalid user type");
      }

      const { data } = response;
      const userData = {
        ...credentials,
        type: userType,
        token: data,
      };

      setToken(data);
      setUser(userData);

      localStorage.setItem("token", data);
      localStorage.setItem("user", JSON.stringify(userData));

      return { success: true };
    } catch (error) {
      return {
        success: false,
        error: error.response?.data || "Login failed",
      };
    }
  };

  const register = async (userData, userType) => {
    try {
      let response;

      switch (userType) {
        case "patient":
          response = await patientAPI.register(userData);
          break;
        case "doctor":
          response = await doctorAPI.register(userData);
          break;
        default:
          throw new Error("Invalid user type");
      }

      return { success: true, data: response.data };
    } catch (error) {
      return {
        success: false,
        error: error.response?.data || "Registration failed",
      };
    }
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  };

  const value = {
    user,
    token,
    loading,
    login,
    register,
    logout,
    isAuthenticated: !!token,
    userType: user?.type,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
