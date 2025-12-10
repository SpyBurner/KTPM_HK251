import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { registerAPI } from '../../services/authServices';

const RegisterForm = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: ''
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleRegister = async () => {
    setError(null);
    setSuccess(false);

    if (!formData.username || !formData.email || !formData.password) {
      setError("Please fill in all fields.");
      return;
    }
    
    if (formData.password !== formData.confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    setLoading(true);

    try {
      const response = await registerAPI({
        username: formData.username,
        email: formData.email,
        password: formData.password
      });

      if (response.code === 1000) {
        // Lưu thông tin đăng nhập
        localStorage.setItem('accessToken', response.result.accessToken);
        localStorage.setItem('refreshToken', response.result.refreshToken);
        localStorage.setItem('user', JSON.stringify(response.result.userDto));
        localStorage.setItem('studentId', response.result.userDto.id.toString());

        // Chuyển hướng về trang chủ
        navigate('/');
        window.location.reload();
      } else {
        setError(response.message || "Registration failed.");
      }
    } catch (err: any) {
      console.error("Register Error:", err);
      if (err.response && err.response.data && err.response.data.message) {
         setError(err.response.data.message);
      } else {
         setError("An error occurred. Please try again.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col gap-5 w-full">
      {error && (
        <div className="bg-red-50 text-red-500 text-xs p-2 rounded border border-red-200">
          {error}
        </div>
      )}
      {success && (
        <div className="bg-green-50 text-green-600 text-xs p-2 rounded border border-green-200">
          Account created successfully! Please Login.
        </div>
      )}

      <div className="flex flex-col gap-2">
        <label className="text-sm font-medium text-gray-900">Username</label>
        <input
          type="text"
          name="username"
          placeholder="Choose a username"
          value={formData.username}
          onChange={handleChange}
          disabled={loading}
          className="w-full px-4 py-3 bg-white border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-black focus:ring-1 focus:ring-black transition-colors placeholder:text-gray-400"
        />
      </div>

      <div className="flex flex-col gap-2">
        <label className="text-sm font-medium text-gray-900">Email</label>
        <input
          type="email"
          name="email"
          placeholder="Enter your email"
          value={formData.email}
          onChange={handleChange}
          disabled={loading}
          className="w-full px-4 py-3 bg-white border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-black focus:ring-1 focus:ring-black transition-colors placeholder:text-gray-400"
        />
      </div>

      <div className="flex flex-col gap-2">
        <label className="text-sm font-medium text-gray-900">Password</label>
        <input
          type="password"
          name="password"
          placeholder="Create a password"
          value={formData.password}
          onChange={handleChange}
          disabled={loading}
          className="w-full px-4 py-3 bg-white border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-black focus:ring-1 focus:ring-black transition-colors placeholder:text-gray-400"
        />
      </div>

      <div className="flex flex-col gap-2">
        <label className="text-sm font-medium text-gray-900">Confirm password</label>
        <input
          type="password"
          name="confirmPassword"
          placeholder="Repeat your password"
          value={formData.confirmPassword}
          onChange={handleChange}
          disabled={loading}
          onKeyDown={(e) => e.key === 'Enter' && handleRegister()}
          className="w-full px-4 py-3 bg-white border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-black focus:ring-1 focus:ring-black transition-colors placeholder:text-gray-400"
        />
      </div>

      <button 
        onClick={handleRegister}
        disabled={loading}
        className={`w-full text-white font-medium py-3 rounded-lg transition-colors mt-2 flex justify-center items-center ${
          loading ? 'bg-gray-500 cursor-not-allowed' : 'bg-[#2c2c2c] hover:bg-black'
        }`}
      >
        {loading ? "Creating Account..." : "Register"}
      </button>
    </div>
  );
};

export default RegisterForm;