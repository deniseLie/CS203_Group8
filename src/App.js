import './App.css';
import Login from './pages/Login';
import { createTheme, ThemeProvider } from '@mui/material';
const theme = createTheme({
  typography: {
    fontFamily: 'Mark, sans-serif', // Define custom font
    

  },
  textfield:{
  }
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <Login/>

    </ThemeProvider>
  );
}

export default App;
