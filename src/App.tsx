import { Routes, Route, Navigate } from 'react-router-dom';
import { LoginPage } from './screens/Login'; 
import { Interface } from './screens/Interface';

function App() {
  // Kiểm tra xem đã có token đăng nhập chưa (để bảo vệ route)
  const isAuthenticated = !!localStorage.getItem('accessToken');

  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />

      <Route 
        path="/" 
        element={
          isAuthenticated ? <Interface /> : <Navigate to="/login" replace />
        } 
      />

      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}

export default App;