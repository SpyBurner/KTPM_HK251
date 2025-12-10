import { Search, X, Check, MoreHorizontal, Image as ImageIcon } from "lucide-react";
import { getAllCourses, Course } from "../../services/authServices";
import { useState, useEffect } from "react";

const activeSidebarTopics = ["Topic 1", "Topic 2", "Topic 3"];
const sortOptions = ["Newest", "Name ascending", "Name descending", "Recommended"];
interface BrowseCoursesProps {
    onNavigate: (page: string, id?: number) => void;
}
const BrowseCoursesBody = ({ onNavigate }: BrowseCoursesProps): JSX.Element => {

  const [courses, setCourses] = useState<Course[]>([]);
  useEffect(() => {
    const fetchCourses = async () => {
      try {
        const data = await getAllCourses();
        setCourses(data);
      } catch (error) {
        console.error("Failed to fetch courses:", error);
      } finally {
      }
    };

    fetchCourses();
  }, []);
  return (
    <>
      <style>{`
        .custom-scrollbar::-webkit-scrollbar {
          width: 6px;
        }
        .custom-scrollbar::-webkit-scrollbar-track {
          background: transparent;
        }
        .custom-scrollbar::-webkit-scrollbar-thumb {
          background-color: #d1d5db; /* gray-300 */
          border-radius: 20px;
        }
        .custom-scrollbar::-webkit-scrollbar-thumb:hover {
          background-color: #9ca3af; /* gray-400 */
        }
      `}</style>


      <div className="w-full max-w-[1100px] mx-auto px-6 py-8 h-[calc(100vh-80px)] overflow-hidden">
        
        <div className="flex gap-10 items-start h-full">
          
          <div className="w-[260px] shrink-0 border border-[#e7e7e8] rounded-lg p-5 flex flex-col gap-5 bg-white shadow-sm h-fit">
            
            <div className="flex items-center gap-3">
              <span className="text-sm font-semibold text-black">Topic</span>
              <div className="relative flex-1">
                <input 
                  type="text" 
                  placeholder="Topic search" 
                  className="w-full bg-[#f9f9f9] border border-[#e5e5e5] rounded-full py-1.5 pl-3 pr-7 text-xs focus:outline-none focus:border-black placeholder:text-gray-400"
                />
                <Search size={12} className="absolute right-2.5 top-1/2 -translate-y-1/2 text-black" />
              </div>
            </div>

            <div className="flex flex-wrap gap-2">
              {activeSidebarTopics.map((topic, index) => (
                <div key={index} className="bg-[#222] text-white text-[11px] font-medium px-2.5 py-1 rounded flex items-center gap-2">
                  <span>{topic}</span>
                  <button className="hover:text-gray-300 flex items-center">
                    <X size={10} strokeWidth={3} />
                  </button>
                </div>
              ))}
            </div>

            <label className="flex items-center gap-2.5 cursor-pointer group pt-1 select-none">
              <div className="relative flex items-center justify-center">
                <input 
                  type="checkbox" 
                  defaultChecked
                  className="peer appearance-none w-4 h-4 border-2 border-[#333] rounded bg-white checked:bg-[#333] transition-all"
                />
                <Check size={10} strokeWidth={4} className="absolute text-white opacity-0 peer-checked:opacity-100 pointer-events-none transition-opacity" />
              </div>
              <span className="text-sm text-[#333] font-medium">
                Show enrolled
              </span>
            </label>
          </div>

          <div className="flex-1 flex flex-col gap-6 h-full overflow-hidden">
            
            <div className="flex flex-row justify-between items-center shrink-0">
              
              <div className="relative w-[280px]">
                 <input 
                    type="text" 
                    placeholder="Course name" 
                    className="w-full bg-white border border-[#e7e7e8] rounded-full py-2 pl-4 pr-10 text-sm focus:outline-none focus:border-black placeholder:text-gray-400"
                  />
                 <Search size={16} className="absolute right-3 top-1/2 -translate-y-1/2 text-black" />
              </div>

              <div className="flex items-center gap-2">
                {sortOptions.map((option, idx) => {
                  const isActive = option === "Newest";
                  return (
                    <button 
                      key={idx}
                      className={`
                        px-3 py-1.5 rounded text-xs font-semibold transition-colors flex items-center gap-1.5 border
                        ${isActive 
                          ? "bg-[#2c2c2c] border-[#2c2c2c] text-white" 
                          : "bg-[#f5f5f5] border-transparent text-[#999] hover:bg-gray-200 hover:text-black"}
                      `}
                    >
                      {isActive && <Check size={12} strokeWidth={3} />}
                      {option}
                    </button>
                  );
                })}
              </div>
            </div>

            <div className="flex-1 overflow-y-auto custom-scrollbar pr-2 pb-10">
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {courses.map((course) => (
                  <div 
                    key={course.id} 
                    onClick={() => onNavigate("course-enroll", course.id)}
                    className="border border-[#e7e7e8] rounded-lg p-3 flex flex-col gap-3 bg-white hover:shadow-md transition-shadow h-fit"
                  >
                    <div className="w-full aspect-square bg-[#e5e5e5] rounded flex items-center justify-center">
                       <ImageIcon className="text-white w-16 h-16 opacity-40" strokeWidth={1.5} />
                    </div>

                    <div className="flex flex-col gap-2.5">
                      <h3 
                      className="font-bold text-sm text-black truncate"
                      title={course.title}
                      >
                        {course.title}</h3>
                      
                      <div className="flex items-center justify-between">
                        <div className="flex gap-1.5">
                          {course.chapters.slice(0, 2).map((chapter, i) => (
                              <span 
                                key={i} 
                                className="bg-[#2c2c2c] text-white text-[10px] font-medium px-2 py-1 rounded max-w-[80px] truncate"
                                title={chapter.title}
                              >
                                {chapter.title}
                              </span>
                            ))}
                            
                            {course.chapters.length > 2 && (
                              <span className="text-[10px] text-gray-500 font-medium py-1">
                                  +{course.chapters.length - 2}
                              </span>
                            )}
                        </div>
                        
                        <button className="text-black hover:bg-gray-100 p-1 rounded-full">
                          <MoreHorizontal size={20} />
                        </button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>

          </div>
        </div>
      </div>
    </>
  );
};

export default BrowseCoursesBody;