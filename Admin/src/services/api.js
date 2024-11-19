import axios from 'axios'; // Import axios for making HTTP requests

/**
 * Axios API Configuration
 *
 * This file exports an `api` instance configured with the base URL for the application's backend.
 * The instance includes default headers for JSON-based communication.
 *
 * Benefits:
 * - Centralized configuration for HTTP requests.
 * - Easy integration with APIs across the application.
 * - Simplifies error handling and request customization.
 */

// Create an Axios instance with default configuration
const api = axios.create({
  // Base URL for the API
  baseURL: 'http://CS203-ALB-1399477535.ap-southeast-1.elb.amazonaws.com:8080',

  // Default headers for all requests
  headers: {
    'Content-Type': 'application/json', // Indicates JSON data for both requests and responses
  },
});

export default api; // Export the configured Axios instance for use throughout the application
