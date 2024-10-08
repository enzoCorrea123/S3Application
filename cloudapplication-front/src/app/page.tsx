"use client"
import Image from "next/image";
import { useRouter } from "next/navigation";
import { FiUpload } from "react-icons/fi";
import { FaTrash } from "react-icons/fa";
import { useEffect, useRef, useState } from "react";
import api from "@/utils/Axios/Axios"
import { IoIosCloseCircleOutline } from "react-icons/io";
import { IoIosCloseCircle } from "react-icons/io";
import { IoClose } from "react-icons/io5";

import * as functions from '../functions';
import { get } from "http";

export default function Home() {
  const router = useRouter();
  const [tasks, setTasks] = useState<Array<TaskGetInterface>>();
  const [image, setImage] = useState<Array<FileGetInterface>>();
  const ref = useRef<HTMLDialogElement>(null);

  const postTask = async (data: FormData) => {
    await functions.postTask(data).then((response) => {
      setTasks([tasks, response])
      getTask();
    });


  }
  const postFile = async (image: any, idTask: number) => {
    console.log("Post File ", image)
    const formData = new FormData();
    formData.append("multipartFile", image.target.files[0])
    await functions.postFile(formData, idTask).then((response) => {
      console.log(response)
    })
  }
  const getFile = async (idTask: number) => {
    await functions.getFile(idTask).then(async (response) => {
      setImage(response)
      await functions.kafkaMessages().then((response) => {
        console.log(response)
      });
      ref.current?.showModal()
    })
  }
  const deleteFile = async (idFile: number) => {
    await functions.deleteFile(idFile).then((response) => {
      if (response.status === 204) {
        setImage(image?.filter((file) => file.idFile !== idFile))

      }
    })
  }

  const deleteTask = async (id: number) => {
    console.log("Entrou deleteTask")
    await functions.deleteTask(id).then((response) => {
      if (response.status === 204) {
        setTasks(tasks?.filter((task) => task.idTask !== id))
        getTask();
      }
    })
  }

  const getTask = async () => {
    console.log("Entrou getTask")
   await functions.getTask().then((response) => {
      console.log(response)
      // if (tasks) {
      //   console.log("Entrou if")
      //   setTasks([...tasks, response])

      // } else {
        console.log("Entrou else")
        setTasks(response)
      }
    // }
    )
  }

  useEffect(() => {
    getTask();
  }, [])

  const renderTasks = () => {
    return (
      tasks?.map((task) => {
        return (
          <div key={task.idTask} className="flex justify-between w-full bg-slate-400 rounded">
            <h1 className="text-lg font-bold ml-3 cursor-pointer" onClick={() => getFile(task.idTask)}>{task.titulo}</h1>
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
      image?.map((file) => {
        return (
          <div className="relative" key={file.idFile}>
            <Image src={file.ref} alt={"imagem"} width={100} height={100} />
            <IoClose className="absolute -top-1 -right-1 bg-[#ff2e26] rounded-full" color="#FFFFFF" size={20} onClick={() => deleteFile(file.idFile)} />
          </div>

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
      <dialog ref={ref} className="rounded">
        <div className="p-3">
          <h1 className="text-xl font-bold text-center">Imagens</h1>
          <div className="flex justify-between items-center gap-3">
            {renderFiles()}
          </div>
          <button onClick={() => ref.current?.close()} className="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 my-2 px-4 border border-blue-500 rounded">Fechar</button>
        </div>
      </dialog>
    </main>
  );
}
