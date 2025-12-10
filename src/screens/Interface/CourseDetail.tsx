import { useState, useEffect } from "react";
import { 
  ChevronDown, 
  ChevronUp, 
  PlayCircle, 
  CheckCircle2, 
  ArrowLeft,
  Image as ImageIcon,
} from "lucide-react";
import { getCourseById, getCourseProgress,Course, Section } from "../../services/authServices";

interface CourseDetailProps {
    courseId: number; 
    onNavigate: (page: string) => void;
}

const CourseDetail = ({ courseId, onNavigate }: CourseDetailProps): JSX.Element => {

  const [course, setCourse] = useState<Course | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  const [progress, setProgress] = useState<number>(0);

  const [expandedChapter, setExpandedChapter] = useState<number | null>(null);
  const [activeSection, setActiveSection] = useState<Section | null>(null);

  useEffect(() => {
      const fetchCourseDetail = async () => {
          if (!courseId) return;
          try {
              setIsLoading(true);
              const studentId = localStorage.getItem('studentId');

              const progressVal = studentId ? await getCourseProgress(parseInt(studentId), courseId) : 0;
              const data = await getCourseById(courseId);

              setCourse(data);
              setProgress(progressVal);

              if (data.chapters && data.chapters.length > 0) {
                  const firstChapter = data.chapters[0];
                  setExpandedChapter(firstChapter.id);
                  if (firstChapter.sections && firstChapter.sections.length > 0) {
                      setActiveSection(firstChapter.sections[0]);
                  }
              }
          } catch (error) {
              console.error("Failed to fetch course detail:", error);
          } finally {
              setIsLoading(false);
          }
      };
      fetchCourseDetail();
  }, [courseId]);

  const toggleChapter = (id: number) => {
    setExpandedChapter(expandedChapter === id ? null : id);
  };

  const handleSelectSection = (section: Section) => {
      setActiveSection(section);
  };

  if (isLoading) return <div className="p-10 text-center">Đang tải dữ liệu khóa học...</div>;
  if (!course) return <div className="p-10 text-center text-red-500">Không tìm thấy khóa học</div>;
  const progressPercentage = Math.round(progress * 100);
  return (
    <div className="w-full h-full bg-white overflow-y-auto custom-scrollbar p-6">
      
<div className="max-w-[1280px] mx-auto mb-6 relative flex items-center justify-center">
        
        <button 
            onClick={() => onNavigate("browse-courses")} 
            className="absolute left-0 flex items-center gap-2 text-gray-500 hover:text-black transition-colors group"
        >
            <div className="p-1.5 rounded-full bg-gray-100 group-hover:bg-gray-200 transition-colors">
                <ArrowLeft size={20} />
            </div>
            <span className="font-medium text-sm hidden md:inline">Back to Your Courses</span>
        </button>

        <h1 className="text-xl font-bold text-gray-800 text-center px-10 truncate max-w-[70%]">
            {course.title}
        </h1>

      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 max-w-[1280px] mx-auto h-[calc(100vh-140px)]">
        
        <div className="lg:col-span-1 flex flex-col gap-4 overflow-y-auto pr-2 custom-scrollbar h-full">
          
          {course.chapters && course.chapters.length > 0 ? (
            course.chapters.map((chapter) => {
                const isExpanded = expandedChapter === chapter.id;

                return (
                    <div key={chapter.id} className="border border-[#e5e7eb] rounded-lg bg-white overflow-hidden shadow-sm">
                        <div 
                            onClick={() => toggleChapter(chapter.id)}
                            className="p-4 flex items-center justify-between cursor-pointer bg-white hover:bg-gray-50 transition-colors select-none"
                        >
                            <div className="flex flex-col gap-1.5">
                                <div className="flex items-center gap-2">
                                    <span className="font-bold text-sm text-gray-900">{chapter.title}</span>
                                    <CheckCircle2 size={16} className="text-gray-300" />
                                </div>
                            </div>
                            
                            {isExpanded ? <ChevronUp size={18} className="text-gray-500"/> : <ChevronDown size={18} className="text-gray-500"/>}
                        </div>

                        {isExpanded && (
                            <div className="px-3 pb-3 pt-0">
                                {chapter.summary && (
                                    <div className="mb-3 text-xs text-gray-500 pl-1">
                                        {chapter.summary}
                                    </div>
                                )}
                                
                                <div className="flex flex-col gap-2">
                                    {chapter.sections && chapter.sections.length > 0 ? (
                                        chapter.sections.map((section) => {
                                            const isActive = activeSection?.id === section.id;
                                            return (
                                                <div 
                                                    key={section.id} 
                                                    onClick={() => handleSelectSection(section)}
                                                    className={`
                                                        group flex items-center justify-between p-3 rounded-md border cursor-pointer transition-all
                                                        ${isActive 
                                                            ? "bg-blue-50 border-blue-200 shadow-sm" 
                                                            : "bg-[#f9fafb] border-transparent hover:border-gray-200 hover:bg-gray-100"
                                                        }
                                                    `}
                                                >
                                                    <div className="flex items-center gap-3 overflow-hidden">
                                                        <div className={`p-1.5 rounded-full shrink-0 ${isActive ? "bg-blue-100 text-blue-600" : "bg-gray-200 text-gray-500 group-hover:bg-white"}`}>
                                                            <PlayCircle size={14} fill={isActive ? "currentColor" : "none"} />
                                                        </div>
                                                        
                                                        <div className="flex flex-col">
                                                            <span className={`text-xs font-semibold ${isActive ? "text-blue-700" : "text-gray-700"}`}>
                                                                {section.name}
                                                            </span>
                                                        </div>
                                                    </div>
                                                </div>
                                            );
                                        })
                                    ) : (
                                        <div className="text-xs text-gray-400 text-center py-2">No lessons yet</div>
                                    )}
                                </div>
                            </div>
                        )}
                    </div>
                );
            })
          ) : (
              <div className="text-center text-gray-500 mt-10">Chưa có nội dung khóa học</div>
          )}
        </div>

        <div className="lg:col-span-2 flex flex-col gap-6 h-full overflow-y-auto custom-scrollbar">
          {activeSection ? (
              <>
                <div className="w-full aspect-video bg-black rounded-xl relative flex items-center justify-center group overflow-hidden shadow-md">
                    <ImageIcon className="text-white/20 w-20 h-20" strokeWidth={1} />
                    <div className="absolute inset-0 flex items-center justify-center bg-black/20 group-hover:bg-black/40 transition-colors cursor-pointer">
                        <PlayCircle size={80} className="text-white opacity-80 group-hover:scale-110 transition-transform duration-300" />
                    </div>
                </div>
                <div className="flex flex-col gap-2">
                    <div className="flex justify-between items-center text-sm font-medium">
                        <span className="text-gray-600">Tiến độ khóa học</span>
                        <span className="text-black">{progressPercentage}% </span>
                    </div>
                    <div className="w-full h-2 bg-gray-200 rounded-full overflow-hidden">
                        <div 
                            className="h-full bg-blue-600 rounded-full transition-all duration-500 ease-out"
                            style={{ width: `${progressPercentage}%` }}
                        ></div>
                    </div>
                </div>
                <div className="flex flex-col gap-4 pb-10">
                    <div className="flex items-center justify-between border-b border-[#e7e7e8] pb-4">
                        <h2 className="text-2xl font-bold text-gray-900">{activeSection.name}</h2>
                    </div>

                    <div className="prose max-w-none text-gray-700 text-sm leading-relaxed">
                        {activeSection.text ? <p>{activeSection.text}</p> : <p className="italic text-gray-400">Không có mô tả cho bài học này.</p>}
                    </div>

                </div>
              </>
          ) : (
              <div className="w-full h-full flex flex-col items-center justify-center text-gray-400 bg-gray-50 rounded-xl border border-dashed border-gray-300">
                  <PlayCircle size={48} className="mb-2 opacity-50" />
                  <p>Chọn một bài học để bắt đầu</p>
              </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default CourseDetail;