import { useState } from 'react';
import { BookOpen } from 'lucide-react';
import LoginForm from './LoginForm';
import RegisterForm from './RegisterForm';

export const LoginPage = (): JSX.Element => {
  const [activeTab, setActiveTab] = useState<'login' | 'register'>('login');

  return (
    <div className="flex flex-col h-screen w-full bg-white overflow-hidden font-sans">
      
      <header className="flex-none h-16 border-b border-[#e7e7e8] bg-white flex items-center px-10">
        <div className="flex items-center justify-center p-2 border border-gray-200 rounded">
           <BookOpen className="w-6 h-6 text-black" />
        </div>
      </header>

      <main className="flex-1 flex flex-col relative">
        
        <div className="relative h-[40%] w-full bg-[#dbeaf0] flex items-center justify-center overflow-hidden">
            <div className="z-10 text-center md:text-left md:w-[630px] px-6">
                <h1 className="text-3xl md:text-4xl font-bold text-black mb-4 tracking-tight">
                  Intelligent Tutoring System
                </h1>
                <p className="text-base text-gray-700">
                  Stop guessing, start learning. The AI tutor that adapts to you.
                </p>
            </div>

            <div className="absolute top-0 right-0 w-1/2 h-full opacity-20 pointer-events-none overflow-hidden">
              <img 
                  src="/left-content.png" 
                  alt=""
                  className='w-full h-full object-cover object-right'
              />
              <div className="absolute inset-0 bg-gradient-to-l from-black/10 to-transparent"></div>
          </div>
        </div>

        <div className="flex-1 bg-white flex justify-center -mt-10 z-20">
           
           <div className="w-full max-w-[400px] flex flex-col items-center">
              
              <div className="flex w-full justify-center mb-0">
                <button
                  onClick={() => setActiveTab('login')}
                  className={`px-8 py-2 text-sm font-medium rounded-t-lg border-b-2 transition-all ${
                    activeTab === 'login'
                      ? 'border-[#303030] text-[#303030] bg-white'
                      : 'border-transparent text-[#767676] hover:text-[#303030]'
                  }`}
                >
                  Login
                </button>
                <button
                  onClick={() => setActiveTab('register')}
                  className={`px-8 py-2 text-sm font-medium rounded-t-lg border-b-2 transition-all ${
                    activeTab === 'register'
                      ? 'border-[#303030] text-[#303030] bg-white'
                      : 'border-transparent text-[#767676] hover:text-[#303030]'
                  }`}
                >
                  Register
                </button>
              </div>

              <div className="w-full bg-white rounded-lg border border-[#d9d9d9] p-6 shadow-sm">
                 {activeTab === 'login' ? <LoginForm /> : <RegisterForm/>}
              </div>

           </div>
        </div>

      </main>
    </div>
  );
};