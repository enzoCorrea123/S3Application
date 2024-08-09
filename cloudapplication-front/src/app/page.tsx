"use client"
import Image from "next/image";
import { useRouter } from "next/navigation";
import { FiUpload } from "react-icons/fi";
import { FaTrash } from "react-icons/fa";
import { useEffect, useRef, useState } from "react";
import api from "@/utils/Axios/Axios"
export default function Home() {
  const router = useRouter();
  const [tasks, setTasks] = useState<Array<TaskGetInterface>>();
  const [image, setImage] = useState<Array<string>>();
  const ref = useRef<HTMLDialogElement>(null);
  const postTask = async (data: FormData) => {
    const taskDto = {
      titulo: data.get("titulo"),
    }
    await api.post("/task", taskDto).then((response) => {
      if (tasks) {
        console.log(response.data)
        setTasks([...tasks, response.data])
      }
    });


  }
  const postFile = async (image: any, idTask: number) => {
    console.log(idTask)
    console.log(image.target.files[0])
    const formData = new FormData();
    formData.append("multipartFile", image.target.files[0])
    await api.post(`/file/${idTask}`, formData).then((response) => {
      console.log(response.data)
    })
  }
  const getFile = async (idTask: number) => {
    await api.get(`/file/${idTask}`).then((response) => {
      setImage(response.data)
    })
    ref.current?.showModal()
  }
  useEffect(() => {
    console.log(tasks)
  }, [tasks])
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
      if (tasks) {
        console.log("Entrou if")
        setTasks([...tasks, response.data])

      } else {
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
            <h1 className="text-lg font-bold ml-3" onClick={() => getFile(task.idTask)}>{task.titulo}</h1>
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

              <label htmlFor={`file-${task.idTask}`}><FiUpload /></label>
              <input type="file" id={`file-${task.idTask}`} className="hidden"
                onChange={(image) => postFile(image, task.idTask)} name="multipartFile" />
              <FaTrash onClick={() => deleteTask(task.idTask)} />
            </div>
          </div>
        )
      })
    )
  }
  const renderFiles = () => {
    return (
      image?.map((ref) => {
        return (
          <Image src={ref} alt={"imagem"} width={100} height={100} />
        )
      }
      )
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
      <dialog ref={ref}>
        {renderFiles()}
        <button onClick={() => ref.current?.close()}>Fechar</button>
      </dialog>
    </main>
  );
}
