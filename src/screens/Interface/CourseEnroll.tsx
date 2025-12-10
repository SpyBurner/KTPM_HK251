import { useState, useEffect } from "react";
import { Heart, Image as ImageIcon, ChevronDown, ChevronUp,PlayCircle,ArrowLeft } from "lucide-react";
import { getCourseById, Course, Chapter, Section } from "../../services/authServices";

interface CourseEnrollProps {
    courseId: number; 
    onNavigate: (page: string) => void;
}
const CourseEnroll = ({ courseId, onNavigate }: CourseEnrollProps): JSX.Element => {

  const [course, setCourse] = useState<Course | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  const [expandedChapter, setExpandedChapter] = useState<number | null>(1);

  useEffect(() => {
      const fetchCourseDetail = async () => {
          if (!courseId) return;
          
          try {
              setIsLoading(true);
              const data = await getCourseById(courseId);
              setCourse(data);
              
              if (data.chapters && data.chapters.length > 0) {
                  setExpandedChapter(data.chapters[0].id);
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
  const renderSectionIcon = () => {
      return <PlayCircle size={14} className="text-gray-500" />;
  };

  if (isLoading) {
      return <div className="p-10 text-center text-gray-500">Đang tải thông tin khóa học...</div>;
  }

  if (!course) {
      return <div className="p-10 text-center text-red-500">Không tìm thấy khóa học</div>;
  }
  return (
    
    <div className="w-full max-w-[1000px] mx-auto px-6 py-10 bg-white">
      <button 
          onClick={() => onNavigate("browse-courses")} 
          className="flex items-center gap-2 text-gray-500 hover:text-black mb-6 transition-colors group"
      >
          <div className="p-1.5 rounded-full bg-gray-100 group-hover:bg-gray-200 transition-colors">
              <ArrowLeft size={20} />
          </div>
          <span className="font-medium text-sm">Back to Courses</span>
      </button>
      <div className="flex flex-col md:flex-row gap-8 mb-10">
        
        <div className="w-full md:w-[450px] shrink-0">
            <div className="w-full aspect-video bg-[#e5e5e5] rounded-lg flex items-center justify-center relative">
                <ImageIcon className="text-white w-16 h-16 opacity-50" strokeWidth={1.5} />
                <button className="absolute top-4 left-4 bg-[#333] p-1.5 rounded-full text-white hover:bg-black transition-colors">
                    <Heart size={16} />
                </button>
            </div>
        </div>

        <div className="flex-1 flex flex-col gap-4">
            <h1 className="text-2xl font-bold text-black">{course.title}</h1>
            
            <div className="flex flex-wrap gap-2">
                {course.chapters && course.chapters.slice(0, 3).map((chapter, i) => (
                    <span key={i} className="bg-[#2c2c2c] text-white text-[11px] font-medium px-3 py-1 rounded-full">
                        {chapter.title}
                    </span>
                ))}
            </div>

            <div className="flex-1"></div> 

            <button className="w-full bg-[#10b981] text-white font-bold py-3 rounded hover:bg-[#059669] transition-colors shadow-sm">
                Enroll
            </button>
        </div>
      </div>

      <div className="mb-10">
          <h3 className="text-lg font-bold text-gray-900 mb-3">Description</h3>
          <div className="text-gray-600 leading-relaxed text-sm">
              {course.description || "No description provided."}
          </div>
      </div>

      <div className="flex flex-col gap-3">
        {course.chapters && course.chapters.length > 0 ? (
            course.chapters.map((chapter: Chapter) => (
                <div key={chapter.id} className="border border-[#e5e5e5] rounded-xl overflow-hidden bg-white shadow-sm">
                    {/* Chapter Header */}
                    <button 
                        onClick={() => toggleChapter(chapter.id)}
                        className="w-full px-5 py-4 flex justify-between items-center bg-white hover:bg-gray-50 transition-colors"
                    >
                        <div className="flex items-center gap-3">
                            <span className="font-bold text-[15px] text-gray-800 text-left">
                                {chapter.title}
                            </span>
                            <span className="text-xs text-gray-400 font-normal">
                                ({chapter.sections ? chapter.sections.length : 0} lectures)
                            </span>
                        </div>
                        {expandedChapter === chapter.id ? <ChevronUp size={18} className="text-gray-400" /> : <ChevronDown size={18} className="text-gray-400" />}
                    </button>

                    {expandedChapter === chapter.id && (
                        <div className="border-t border-[#f0f0f0] bg-[#fcfcfc]">
                            {chapter.summary && (
                                <div className="px-5 py-3 text-xs text-gray-500 italic border-b border-[#f0f0f0]">
                                    {chapter.summary}
                                </div>
                            )}

                            <div className="flex flex-col">
                                {chapter.sections && chapter.sections.length > 0 ? (
                                    chapter.sections.map((section: Section) => (
                                        <div 
                                            key={section.id} 
                                            className="px-5 py-3 flex items-start gap-3 hover:bg-gray-100 transition-colors cursor-pointer border-b border-[#f0f0f0] last:border-0"
                                        >
                                            <div className="mt-1 shrink-0">
                                                {renderSectionIcon()}
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-700 font-medium">
                                                    {section.name}
                                                </p>
                                                {section.text && (
                                                    <p className="text-xs text-gray-400 mt-0.5 line-clamp-1">
                                                        {section.text}
                                                    </p>
                                                )}
                                            </div>
                                        </div>
                                    ))
                                ) : (
                                    <div className="px-5 py-4 text-xs text-gray-400 text-center">
                                        No lessons updated yet.
                                    </div>
                                )}
                            </div>
                        </div>
                    )}
                </div>
            ))
        ) : (
            <div className="text-gray-500 italic">No chapters available.</div>
        )}
    </div>

    </div>
  );
};

export default CourseEnroll;