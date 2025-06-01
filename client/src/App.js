import { Route, Routes } from 'react-router-dom';
import './App.css';
import Layout from './components/Layout';
import Login from './components/Login';
import PrivateRoute from './components/PrivateRoute';
import Register from './components/Register';
import Shipping from './components/Shipping';

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route path="/login" element={<Login />} />
          <Route path="/shipping" element={
            <PrivateRoute requiredRole="ROLE_ADMIN">
              <Shipping />
            </PrivateRoute>
          }
          />
          <Route path="/register" element={
            <PrivateRoute requiredRole="ROLE_ADMIN">
              <Register />
            </PrivateRoute>
          } />
        </Route>
      </Routes>
    </div>
  );
}

export default App;
