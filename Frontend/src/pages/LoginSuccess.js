import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { useAuth } from '../auth/AuthProvider';
// this page is for google oauth
const LoginSuccess = () => {
  const navigate = useNavigate();

  const { login } = useAuth(); // Get login function from AuthProvider
  useEffect(() => {
    // Get the token from the URL
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    if (token) {
      // Call the login function from AuthProvider to set the user state
      login(token);

      // Redirect to the find tournament page
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
