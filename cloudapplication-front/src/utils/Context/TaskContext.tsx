"use client";
import { ReactNode, createContext, useContext, useState } from "react";

const AppContext = createContext({});

export function TaskContext({ children }: { children: ReactNode }) {
  const [arrayId, setArrayId] = useState<Array<number>>()

  return (
    <AppContext.Provider value={{arrayId, setArrayId}}>
      {children}
    </AppContext.Provider>
  );
}

export function useTaskContext() {
  return useContext(AppContext);
}

