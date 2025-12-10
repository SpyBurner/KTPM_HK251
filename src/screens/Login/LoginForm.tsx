import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginAPI } from '../../services/authServices';

const LoginForm = () => {
  const navigate = useNavigate();
  
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleLogin = async () => {
    setError(null);
    
    if (!username || !password) {
      setError("Please enter both email/username and password.");
      return;
    }

    setLoading(true);

    try {
      const data = await loginAPI(username, password);

      if (data.code === 1000) {
        localStorage.setItem('accessToken', data.result.accessToken);
        localStorage.setItem('refreshToken', data.result.refreshToken);
        localStorage.setItem('user', JSON.stringify(data.result.userDto));
        
        localStorage.setItem('studentId', data.result.userDto.id.toString());
        localStorage.setItem('userInfo', JSON.stringify(data.result.userDto));

        navigate('/'); 
        window.location.reload();
      } else {
        setError(data.message || "Login failed. Please check your credentials.");
      }
    } catch (err: any) {
      console.error("Login error:", err);
      setError("An error occurred connecting to the server.");
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

      <div className="flex flex-col gap-2">
        <label className="text-sm font-medium text-gray-900">Email / Username</label>
        <input
          type="text" 
          placeholder="Enter your username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          disabled={loading}
          className="w-full px-4 py-3 bg-white border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-black focus:ring-1 focus:ring-black transition-colors placeholder:text-gray-400"
        />
      </div>

      <div className="flex flex-col gap-2">
        <label className="text-sm font-medium text-gray-900">Password</label>
        <input
          type="password"
          placeholder="Enter your password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          disabled={loading}
          onKeyDown={(e) => e.key === 'Enter' && handleLogin()}
          className="w-full px-4 py-3 bg-white border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-black focus:ring-1 focus:ring-black transition-colors placeholder:text-gray-400"
        />
      </div>

      {/* Remember Me */}
      <div className="flex items-center gap-3">
        <input
          type="checkbox"
          id="remember"
          defaultChecked
          className="w-4 h-4 rounded border-gray-300 text-black focus:ring-black cursor-pointer accent-black"
        />
        <label htmlFor="remember" className="text-sm text-gray-900 cursor-pointer select-none">
          Remember me
        </label>
      </div>

      <button 
        onClick={handleLogin}
        disabled={loading}
        className={`w-full text-white font-medium py-3 rounded-lg transition-colors mt-2 flex justify-center items-center ${
          loading ? 'bg-gray-500 cursor-not-allowed' : 'bg-[#2c2c2c] hover:bg-black'
        }`}
      >
        {loading ? (
          <svg className="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
        ) : (
          "Login"
        )}
      </button>
    </div>
  );
};

export default LoginForm;