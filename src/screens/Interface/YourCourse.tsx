import { useEffect, useState } from "react";
import { Plus, Search, X, Image as ImageIcon } from "lucide-react";
import { getEnrolledCourses, getCourseProgress, EnrolledCourse } from "../../services/authServices";

const activeTopics = ["Topic 1", "Topic 2", "Topic 3"];

interface YourCoursesProps {
    onNavigate: (page: string, id?: number) => void;
}

const YourCourses = ({ onNavigate }: YourCoursesProps): JSX.Element => {
  const [courses, setCourses] = useState<EnrolledCourse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMyCourses = async () => {
      try {
        const studentIdStr = localStorage.getItem('studentId');
        
        if (studentIdStr) {
          const studentId = parseInt(studentIdStr);

          const enrolledData = await getEnrolledCourses(studentId);

          const coursesWithProgress = await Promise.all(
            enrolledData.map(async (course) => {
              try {
                const progressVal = await getCourseProgress(studentId, course.id);
                return { ...course, progress: progressVal * 100 };
              } catch (err) {
                console.error(`Failed to load progress for course ${course.id}`, err);
                return { ...course, progress: 0 };
              }
            })
          );

          setCourses(coursesWithProgress);
        }
      } catch (error) {
        console.error("Failed to load your courses:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchMyCourses();
  }, []);

  if (loading) {
    return <div className="p-10 text-center text-gray-500">Loading your learning path...</div>;
  }

  return (
    <div className="flex flex-col w-full h-full bg-white overflow-hidden">
      
      <div className="flex justify-center items-center py-6 border-b border-transparent shrink-0">
        <h1 className="text-xl font-bold text-black">Your courses</h1>
      </div>

      <div className="flex flex-row gap-8 px-8 pb-8 w-full h-full overflow-hidden">
        
        <div className="flex-1 flex flex-col h-full overflow-hidden">
            
            <div className="flex justify-end mb-4 shrink-0">
                <button className="border-2 border-black rounded p-1 hover:bg-gray-100 transition-colors">
                    <Plus size={20} className="text-black" strokeWidth={3} />
                </button>
            </div>

            <div className="flex-1 overflow-y-auto pr-2 pb-20 custom-scrollbar flex flex-col gap-4">
                {courses.length > 0 ? (
                  courses.map((course) => (
                      <div 
                          key={course.id} 
                          className="border border-[#e7e7e8] rounded-lg p-4 flex flex-row gap-5 items-center bg-white"
                      >
                          <div className="w-28 h-20 bg-[#e5e5e5] rounded flex items-center justify-center shrink-0">
                              <ImageIcon className="text-white w-8 h-8" />
                          </div>

                          <div className="flex flex-col gap-2 flex-1">
                              <h3 className="font-bold text-base text-black">{course.title}</h3>
                              
                              <div className="flex items-center gap-4 w-full">
                                  <div className="flex-1 h-1.5 bg-[#e7e7e8] rounded-full overflow-hidden">
                                      <div 
                                          className="h-full bg-[#0088ff] rounded-full transition-all duration-500" 
                                          style={{ width: `${course.progress || 0}%` }}
                                      ></div>
                                  </div>
                                  
                                  <button 
                                      onClick={() => onNavigate("course-detail", course.id)}
                                      className="bg-[#333333] text-white text-xs font-medium px-4 py-1.5 rounded hover:bg-black transition-colors shrink-0"
                                  >
                                      Continue
                                  </button>
                              </div>
                          </div>
                      </div>
                  ))
                ) : (
                  <div className="text-center mt-10 text-gray-400">
                    You haven't enrolled in any courses yet.
                  </div>
                )}
            </div>
        </div>

        <div className="w-[280px] shrink-0 h-fit">
            <div className="border border-[#e7e7e8] rounded-lg p-4 flex flex-col gap-4 bg-white shadow-[0_2px_8px_rgba(0,0,0,0.02)]">
                
                <div className="flex items-center gap-2">
                    <span className="text-sm font-semibold text-black">Topic</span>
                    <div className="relative flex-1">
                        <input 
                            type="text" 
                            placeholder="Topic search" 
                            className="w-full bg-[#f5f5f5] rounded-full py-1.5 pl-3 pr-7 text-xs focus:outline-none focus:ring-1 focus:ring-gray-300 placeholder:text-gray-400"
                        />
                        <Search size={12} className="absolute right-2.5 top-1/2 -translate-y-1/2 text-gray-400" />
                    </div>
                </div>

                <div className="flex flex-wrap gap-2">
                    {activeTopics.map((topic, index) => (
                        <div key={index} className="bg-[#2c2c2c] text-white text-[11px] px-2.5 py-1 rounded flex items-center gap-1.5">
                            <span>{topic}</span>
                            <button className="hover:text-gray-300 flex items-center">
                                <X size={10} strokeWidth={3} />
                            </button>
                        </div>
                    ))}
                </div>

                <div className="flex flex-col gap-2 pt-1">
                    {["Starred", "Finished", "Saved"].map((label) => (
                        <label key={label} className="flex items-center gap-2.5 cursor-pointer group">
                            <div className="relative flex items-center justify-center">
                                <input 
                                    type="checkbox" 
                                    className="peer appearance-none w-4 h-4 border-2 border-[#e7e7e8] rounded bg-white checked:bg-black checked:border-black transition-all"
                                />
                                <svg className="absolute w-2.5 h-2.5 text-white opacity-0 peer-checked:opacity-100 pointer-events-none transition-opacity" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="4" strokeLinecap="round" strokeLinejoin="round">
                                    <polyline points="20 6 9 17 4 12"></polyline>
                                </svg>
                            </div>
                            <span className="text-sm text-[#555] group-hover:text-black transition-colors font-medium">
                                {label}
                            </span>
                        </label>
                    ))}
                </div>

            </div>
        </div>

      </div>
    </div>
  );
};

export default YourCourses;