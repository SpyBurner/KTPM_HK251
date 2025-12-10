import  { useState, useRef, useEffect } from "react";
import { Camera, Plus, MoreVertical ,File} from "lucide-react";

interface UserDto {
  id: number;
  username: string;
  role: string;
}
const ProfilePage = (): JSX.Element => {

  const [user, setUser] = useState<UserDto | null>(null);

  const blueShadow = "shadow-[0_10px_30px_-4px_rgba(59,130,246,0.25),0_6px_12px_-4px_rgba(59,130,246,0.2)]";

  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const storedUser = localStorage.getItem('userInfo');
        if (storedUser) {
            try {
                const parsedUser = JSON.parse(storedUser);
                setUser(parsedUser);
                
            } catch (error) {
                console.error("Lỗi parse thông tin user", error);
            }
        }
    }, []);
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsDropdownOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  return (
    <div className="w-full max-w-[1280px] mx-auto px-6 py-10">
      
      <div className="relative flex flex-col items-center justify-center mb-16">
        <div className="w-32 h-32 rounded-full overflow-hidden mb-4 border-4 border-white shadow-sm">
          <img src="/image.png" alt="Profile" className="w-full h-full object-cover" />
        </div>
        <h1 className="text-2xl font-bold text-black">{user?.username || "Guest"}</h1>
        <p className="text-gray-500 text-sm mt-1">{user?.role || "Visitor"}</p>
<div 
            ref={dropdownRef}
            className="absolute right-0 top-1/2 -translate-y-1/2"
        >
            <button 
                onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                className={`bg-[#2c2c2c] text-white text-xs font-medium px-4 py-2 rounded flex items-center gap-2 transition-colors ${isDropdownOpen ? 'bg-black' : 'hover:bg-black'}`}
            >
              <Camera size={14} /> Change image
            </button>
            {isDropdownOpen && (
                <div className="absolute top-full right-0 mt-2 w-[340px] bg-white border border-[#e7e7e8] rounded-xl shadow-xl p-6 z-50 animate-in fade-in zoom-in-95 duration-200">
                    <h3 className="text-xl font-bold text-gray-900 mb-6 text-center">
                        Upload new profile picture
                    </h3>
                    <div className="flex items-center justify-between px-1">
                        <div className="flex items-center gap-3">
                            <File size={24} strokeWidth={1.5} className="text-[#333]" />
                            <span className="text-sm font-medium text-gray-600">abc.png</span>
                        </div>
                        <button className="bg-[#2c2c2c] text-white text-sm font-medium px-6 py-2.5 rounded-lg hover:bg-black transition-colors shadow-sm">
                            Continue
                        </button>
                    </div>
                    <div className="absolute -top-1.5 right-6 w-3 h-3 bg-white border-t border-l border-[#e7e7e8] transform rotate-45"></div>
                </div>
            )}
        </div>
      </div>

      <h2 className="text-center text-lg font-bold text-gray-800 mb-8">Your performance</h2>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        <div className={`lg:col-span-2 bg-white border border-[#e7e7e8] rounded-xl p-6 relative overflow-hidden h-[450px] flex flex-col ${blueShadow}`}>
          <div className="flex justify-between items-center mb-6 relative z-10 shrink-0">
            
            <div className="flex items-center gap-5">
                <button className="text-[#333] hover:bg-gray-100 p-1 rounded transition-colors">
                    <MoreVertical size={20} strokeWidth={2.5} />
                </button>
                
                <div className="flex gap-3">
                    <div className="relative">
                        <input 
                            type="text" 
                            placeholder="Search" 
                            className="bg-[#eef2f6] text-sm px-4 py-2.5 rounded font-medium w-80 focus:outline-none focus:ring-1 focus:ring-blue-300 placeholder:text-gray-400 text-gray-600" 
                        />
                    </div>
                    <button className="bg-[#3b82f6] text-white w-[38px] h-[38px] flex items-center justify-center rounded hover:bg-blue-700 shadow-md transition-all">
                        <Plus size={20} strokeWidth={3} />
                    </button>
                </div>
            </div>

            <div className="flex flex-col items-end justify-center">
                <h3 className="font-bold text-gray-800 uppercase  text-xl leading-none">
                    USER NAME
                </h3>
                <p className="text-[9px] text-gray-400 font-medium mt-1">
                    minim veniam quis nostrud
                </p>
            </div>
          </div>

          <div className="absolute top-[100px] left-[20%] text-center z-10">
              <span className="font-bold text-gray-700 text-lg">425</span>
              <p className="text-[10px] text-gray-400">deserunt</p>
          </div>
          <div className="absolute top-[120px] left-[50%] text-center z-10">
              <span className="font-bold text-gray-700 text-lg">365</span>
              <p className="text-[10px] text-gray-400">commodo</p>
          </div>
          <div className="absolute top-[160px] left-[80%] text-center z-10">
              <span className="font-bold text-gray-700 text-lg">268</span>
              <p className="text-[10px] text-gray-400">laborum</p>
          </div>

          <div className="flex-1 w-full relative">
            <svg viewBox="0 0 1000 400" className="w-full h-full absolute bottom-0" preserveAspectRatio="none">
                <defs>
                    <linearGradient id="chartGradient" x1="0%" y1="0%" x2="0%" y2="100%">
                        <stop offset="0%" stopColor="#8b5cf6" stopOpacity="0.9" />
                        <stop offset="50%" stopColor="#6366f1" stopOpacity="0.8" />
                        <stop offset="100%" stopColor="#3b82f6" stopOpacity="0.9" />
                    </linearGradient>
                </defs>
                <path d="M0,300 Q100,250 200,280 T400,250 T600,280 T800,260 T1000,300 V400 H0 Z" fill="#a5b4fc" opacity="0.4"/>
                <path d="M0,280 Q150,200 300,260 T500,220 T700,270 T900,240 T1000,280 V400 H0 Z" fill="#818cf8" opacity="0.6"/>
                <path d="M0,250 C50,240 100,320 150,280 C200,150 250,250 300,220 C350,200 450,300 550,180 C600,150 650,280 750,260 C800,200 850,260 900,240 L1000,250 V400 H0 Z" fill="url(#chartGradient)" />
                <line x1="225" y1="180" x2="225" y2="400" stroke="white" strokeWidth="2" strokeDasharray="5 5" opacity="0.3" />
                <line x1="550" y1="180" x2="550" y2="400" stroke="white" strokeWidth="2" strokeDasharray="5 5" opacity="0.3" />
                <line x1="880" y1="240" x2="880" y2="400" stroke="white" strokeWidth="2" strokeDasharray="5 5" opacity="0.3" />
            </svg>
          </div>
            
          <div className="flex justify-between px-2 mt-4 shrink-0">
            {[...Array(15)].map((_, i) => (
                <span key={i} className="text-[10px] font-bold text-black w-6 text-center">
                    {i + 1 < 10 ? `0${i+1}` : i+1}
                </span>
            ))}
          </div>

        </div>

        <div className="flex flex-col gap-6 h-[450px]">
            
            <div className={`bg-white border border-[#e7e7e8] rounded-xl p-6 flex-1 flex flex-row items-center gap-6 ${blueShadow}`}>
                 
                 <div className="flex flex-col justify-center gap-6 shrink-0">
                    <div className="flex items-center gap-3">
                        <div className="w-3 h-3 bg-[#3b82f6] rounded-sm shrink-0"></div>
                        <div>
                            <h4 className="text-xl font-bold text-gray-800 leading-none">128</h4>
                            <p className="text-[9px] text-gray-400 uppercase">deserunt</p>
                        </div>
                    </div>
                    <div className="flex items-center gap-3">
                        <div className="w-3 h-3 bg-[#10b981] rounded-sm shrink-0"></div>
                        <div>
                            <h4 className="text-xl font-bold text-gray-800 leading-none">834</h4>
                            <p className="text-[9px] text-gray-400 uppercase">laborum</p>
                        </div>
                    </div>
                    <div className="flex items-center gap-3">
                        <div className="w-3 h-3 bg-[#f97316] rounded-sm shrink-0"></div>
                        <div>
                            <h4 className="text-xl font-bold text-gray-800 leading-none">556</h4>
                            <p className="text-[9px] text-gray-400 uppercase">commodo</p>
                        </div>
                    </div>
                 </div>

                 <div className="flex items-end gap-2 h-32 pb-1 flex-1 w-full">
                     {[
                        { o: 40, g: 30, b: 20 }, { o: 20, g: 40, b: 10 }, { o: 50, g: 30, b: 15 },
                        { o: 30, g: 50, b: 20 }, { o: 60, g: 10, b: 30 }, { o: 45, g: 25, b: 30 },
                        { o: 70, g: 20, b: 10 }, { o: 80, g: 10, b: 10 }, { o: 50, g: 30, b: 10 },
                     ].map((h, i) => (
                        <div key={i} className="flex flex-col items-center gap-1 flex-1">
                            <div className="flex flex-col-reverse w-full max-w-[14px] h-24 bg-gray-50/50 rounded-t-sm overflow-hidden">
                                <div style={{ height: `${h.o}%` }} className="bg-[#f97316] w-full border-t border-white/50"></div>
                                <div style={{ height: `${h.g}%` }} className="bg-[#10b981] w-full border-t border-white/50"></div>
                                <div style={{ height: `${h.b}%` }} className="bg-[#3b82f6] w-full border-t border-white/50"></div>
                            </div>
                            <span className="text-[9px] text-gray-400 font-bold">{i + 1}</span>
                        </div>
                     ))}
                 </div>
            </div>

            <div className={`bg-white border border-[#e7e7e8] rounded-xl p-6 flex-1 flex flex-col ${blueShadow}`}>
                <div className="flex justify-between items-stretch gap-4 w-full h-40">
                    {[
                        { day: "Mon", h1: 30, h2: 45, h3: 25 }, { day: "Tue", h1: 45, h2: 60, h3: 50 },
                        { day: "Wed", h1: 35, h2: 70, h3: 40 }, { day: "Thu", h1: 60, h2: 80, h3: 90 },
                        { day: "Fri", h1: 75, h2: 55, h3: 85 },
                    ].map((item) => (
                        <div key={item.day} className="flex flex-col items-center gap-3 w-full h-full justify-end">
                            <div className="flex items-end gap-1.5 flex-1 pb-1">
                                <div className="w-2.5 bg-[#3b82f6] rounded-t-sm transition-all duration-500 hover:opacity-80" style={{ height: `${item.h1}%` }}></div>
                                <div className="w-2.5 bg-[#10b981] rounded-t-sm transition-all duration-500 hover:opacity-80" style={{ height: `${item.h2}%` }}></div>
                                <div className="w-2.5 bg-[#e2e8f0] rounded-t-sm transition-all duration-500 hover:opacity-80" style={{ height: `${item.h3}%` }}></div>
                            </div>
                            <span className="text-[11px] font-bold text-gray-700">{item.day}</span>
                        </div>
                    ))}
                </div>
            </div>

        </div>

      </div>
    </div>
  );
};

export default ProfilePage;