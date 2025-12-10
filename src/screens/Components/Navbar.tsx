import { useState, useRef, useEffect } from "react";
interface UserDto {
  id: number;
  username: string;
  role: string;
}
interface SidebarProps {
    activePage: string;
    onNavigate: (pageName: string) => void;
}

export const Navbar = ({ activePage, onNavigate }: SidebarProps): JSX.Element => {

    const [user, setUser] = useState<UserDto | null>(null);

    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const dropdownRef = useRef<HTMLDivElement>(null);

    const menuItem = [
        { label: "home", name: "HOME" },
        { label: "your-courses", name: "YOUR COURSES" },
        { label: "browse-courses", name: "BROWSE COURSES" }
    ];
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
    const handleLogout = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('userInfo'); // Xóa thông tin user
        console.log("Logged out");
        // Điều hướng về trang login hoặc reload trang
        window.location.href = '/login'; 
    };

    const handleNavigation = (page: string) => {
        if (page === "logout") {
            localStorage.clear();
        }
        onNavigate(page);
        setIsDropdownOpen(false); 
    };

    return (
        <aside className="flex w-screen justify-between border-b border-[#e7e7e8] h-[80px] bg-white relative z-50">
            <div className="w-[80px] h-full flex items-center justify-center border-r border-[#e7e7e8] shrink-0">
                <img src="/book.svg" alt="book" className="w-8 h-8"/>
            </div>

            <nav className="flex h-full items-center">
                {menuItem.map((item, index) => {
                    const isActive = item.label === activePage || (activePage === "course-detail" && item.label === "your-courses") || (activePage === "course-enroll" && item.label === "browse-courses");
                    return (
                        <button 
                            key={index}
                            onClick={() => onNavigate(item.label)}
                            className={`h-full px-6 hover:text-[#0088ff] transform transition hover:font-semibold flex items-center ${
                                isActive ? "text-[#0088ff] font-semibold" : "text-[#000]"
                            }`}
                        >
                            {item.name}
                        </button>
                    );
                })}
            </nav>

            <div 
                ref={dropdownRef}
                className="relative w-[220px] h-full border-l border-[#e7e7e8] shrink-0"
            >
                <button 
                    onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                    className="flex items-center justify-center gap-3 w-full h-full hover:bg-gray-50 transition-colors px-4"
                >
                    <img 
                        src="/image.png" 
                        alt="avatar" 
                        className="w-[40px] h-[40px] rounded-full object-cover"
                    />
                    <div className="text-left">
                        <h4 className="text-[#333] font-bold text-sm leading-tight">{user?.username || "Guest"}</h4>
                        <p className="text-[#b3b3b3] text-xs">{user?.role || "Visitor"}</p>
                    </div>
                </button>

                {isDropdownOpen && (
                    <div className="absolute top-[90px] right-4 w-[200px] bg-white shadow-[0_4px_20px_rgba(0,0,0,0.1)] border border-[#e7e7e8] rounded-xl p-4 flex flex-col gap-3 items-center animate-in fade-in zoom-in-95 duration-200">
                        
                        <button 
                            onClick={() => handleNavigation("profile")} 
                            className="w-fit min-w-[100px] bg-[#2c2c2c] text-white font-medium py-2 px-6 rounded-lg hover:bg-black transition-colors"
                        >
                            Profile
                        </button>

                        <button 
                            onClick={handleLogout}
                            className="w-fit min-w-[100px] bg-[#ef4444] text-white font-medium py-2 px-6 rounded-lg hover:bg-red-600 transition-colors"
                        >
                            Logout
                        </button>

                    </div>
                )}
            </div>
        </aside>
    );
};