// import { Navbar } from "../Components";
// import { useState } from "react";
// import Home from "./Home";
// import YourCourses from "./YourCourse";
// import BrowseCourses from "./BrowseCourse";
// import Profile from "./Profile";
// import CourseDetail from "./CourseDetail";
// import CourseEnroll from "./CourseEnroll";

// export const Interface = () : JSX.Element => {
//     const [activePage, setActivePage] = useState("home");
//     const renderContent = () => {
//         switch(activePage) {
//             case "home":
//                 return <Home/>
//             case "your-courses":
//                 return <YourCourses onNavigate={setActivePage} />
//             case "browse-courses":
//                 return <BrowseCourses onNavigate={setActivePage} />;
//             case "profile":
//                 return <Profile/>
//             case "course-detail":
//                 return <CourseDetail/>
//             case "course-enroll":
//                 return <CourseEnroll />;
//             default:
//                 return <div className="p-8">Chức năng đang phát triển: {activePage}</div>;
//         }
//     }

//     return (
//         <div className="flex flex-col h-screen overflow-hidden">
//             <Navbar activePage={activePage} onNavigate={setActivePage}/>
//             <main className="flex-1 overflow-auto">
//                 {renderContent()}
//             </main>
//         </div>
//     )
// }

// export default Interface;


import { Navbar } from "../Components";
import { useState } from "react";
import Home from "./Home";
import YourCourses from "./YourCourse";
import BrowseCourses from "./BrowseCourse";
import Profile from "./Profile";
import CourseDetail from "./CourseDetail";
import CourseEnroll from "./CourseEnroll";

export const Interface = () : JSX.Element => {
    const [activePage, setActivePage] = useState("home");
    
    const [selectedCourseId, setSelectedCourseId] = useState<number | null>(null);

    const handleNavigate = (page: string, id?: number) => {
        setActivePage(page);
        if (id) {
            setSelectedCourseId(id);
            console.log("Đã chọn khóa học ID:", id); 
        }
    };

    const renderContent = () => {
        switch(activePage) {
            case "home":
                return <Home onNavigate={handleNavigate} />
            case "your-courses":
                return <YourCourses onNavigate={handleNavigate} />
            case "browse-courses":
                return <BrowseCourses onNavigate={handleNavigate} />;
            case "profile":
                return <Profile/>
            case "course-detail":
                return selectedCourseId ? <CourseDetail courseId={selectedCourseId} onNavigate={(page) => handleNavigate(page)} /> : <div>Chưa chọn khóa học</div>;
            case "course-enroll":
                return selectedCourseId ? <CourseEnroll courseId={selectedCourseId} onNavigate={(page) => handleNavigate(page)} /> : <div>Chưa chọn khóa học</div>;
            default:
                return <div className="p-8">Chức năng đang phát triển: {activePage}</div>;
        }
    }

    return (
        <div className="flex flex-col h-screen overflow-hidden">
            <Navbar activePage={activePage} onNavigate={(page) => handleNavigate(page)}/>
            <main className="flex-1 overflow-auto">
                {renderContent()}
            </main>
        </div>
    )
}

export default Interface;