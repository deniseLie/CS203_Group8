import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const LoginSuccess = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // Get the token from the URL
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    if (token) {
      // Store the token in localStorage for future use
      localStorage.setItem('jwtToken', token);

      // Redirect to the main app or dashboard
      navigate('/');
    } else {
      // If no token is found, redirect to login or display an error
      navigate('/login');
    }
  }, [navigate]);

  return (
    <div>
      <p>Logging you in...</p>
    </div>
  );
};

export default LoginSuccess;
