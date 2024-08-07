"use client"
import Image from "next/image";
import { useRouter } from "next/navigation";
import { FiUpload } from "react-icons/fi";
import { FaTrash } from "react-icons/fa";
import { useEffect, useState } from "react";
import api from "@/utils/Axios/Axios"
export default function Home() {
  const router = useRouter();
  const [tasks, setTasks] = useState<Array<TaskGetInterface>>();
  const postTask = async (data: FormData) => {
    const taskDto = {
      titulo: data.get("titulo"),
    }
    await api.post("/task", taskDto).then((response) => {
      if(tasks){
        console.log(response.data)
        setTasks([...tasks, response.data])
      }
    });
    

  }
  useEffect(() => {
    console.log(tasks)
  },[tasks])
  const deleteTask = async (id: number) => {
    console.log(id)
    await api.delete(`/task/${id}`).then((response) => {
      if (response.status === 204) {
        setTasks(tasks?.filter((task) => task.idTask !== id))
      }
    })
  }
  useEffect(() => {
    api.get("/task").then((response) => {
      console.log(response.data)
      if(tasks){
        console.log("Entrou if")
        setTasks([...tasks, response.data])

      }else{
        console.log("Entrou else")
        setTasks(response.data)
      }
    })
  }, [])
  const renderTasks = () => {
    return (
      tasks?.map((task) => {
        return (
          <div key={task.idTask} className="flex justify-between w-full bg-slate-400 rounded">
            <h1 className="text-lg font-bold ml-3">{task.titulo}</h1>
            {/* <div className="flex flex-col gap-4">
                      {task.files.map((file)=>{
                          return(
                              <div key={file.id} className="flex flex-col gap-4">
                                  <h1 className="text-lg font-bold">{file.nome}</h1>
                                  <img src={file.url} alt={file.nome} className="w-1/4"/>
                              </div>
                          )
                      })}
                  </div> */}  
            <div className="flex items-center gap-3 mr-3">
                
                <label htmlFor="file"><FiUpload /></label>
                <input type="file" id="file" className="hidden" name="multipartFile"/>
              <FaTrash onClick={()=>deleteTask(task.idTask)}/>
            </div>
          </div>
        )
      })
    )
  }
  return (
    <main>
      <header className="p-4 bg-gray-800 text-white">
        <h1 className="text-2xl" onClick={() => router.push("/")}>Cloud-Frontend</h1>
      </header>
      <div className="flex flex-col justify-center items-center min-h-screen gap-4">

        <div className="flex flex-col justify-center items-center gap-4 w-1/4 bg-slate-400 rounded">
          <h1 className="text-lg font-bold">Create Task</h1>
          <form action={(data) => postTask(data)} className="flex flex-col">
            <label>Título da task</label>
            <input type="text" name="titulo" className="p-1 w-full" />
            <button type="submit" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 my-2 px-4 border border-blue-500 rounded">Adicionar task</button>
          </form>
        </div>

        <div className="flex flex-col gap-2 w-1/4">
          {renderTasks()}
        </div>
      </div>
    </main>
  );
}
