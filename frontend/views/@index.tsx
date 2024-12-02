import {useEffect, useState} from "react";
import {AssistantService, AppointmentUIService} from "Frontend/generated/endpoints";
import AppointmentDetails from "../generated/ai/spring/demo/ai/playground/data/Appointment";

import {GridColumn} from "@vaadin/react-components/GridColumn";
import {Grid} from "@vaadin/react-components/Grid";
import {MessageInput} from "@vaadin/react-components/MessageInput";
import {nanoid} from "nanoid";
import {SplitLayout} from "@vaadin/react-components/SplitLayout";
import Message, {MessageItem} from "../components/Message";
import MessageList from "Frontend/components/MessageList";

export default function Index() {
  const [chatId, setChatId] = useState(nanoid());
  const [working, setWorking] = useState(false);
  const [appointments, setAppointments] = useState<AppointmentDetails[]>([]);
  const [messages, setMessages] = useState<MessageItem[]>([{
    role: 'assistant',
    content: 'Welcome to ACME Clinic! How can I help you?'
  }]);

  useEffect(() => {
    if (!working) {
      AppointmentUIService.getAppointments().then(setAppointments);
    }
  }, [working]);

  function addMessage(message: MessageItem) {
    setMessages(messages => [...messages, message]);
  }

  function appendToLatestMessage(chunk: string) {
    setMessages(messages => {
      const latestMessage = messages[messages.length - 1];
      latestMessage.content += chunk;
      return [...messages.slice(0, -1), latestMessage];
    });
  }

  async function sendMessage(message: string) {
    setWorking(true);
    addMessage({
      role: 'user',
      content: message
    });
    let first = true;
    AppointmentUIService.chat(chatId, message)
      .onNext(token => {
        if (first && token) {
          addMessage({
            role: 'assistant',
            content: token
          });

          first = false;
        } else {
          appendToLatestMessage(token);
        }
      })
      .onError(() => setWorking(false))
      .onComplete(() => setWorking(false));
  }

  return (
    <SplitLayout className="h-full">
      <div className="flex flex-col gap-m p-m box-border h-full" style={{width: '30%'}}>
        <h3>Provider support</h3>
        <MessageList messages={messages} className="flex-grow overflow-scroll"/>
        <MessageInput onSubmit={e => sendMessage(e.detail.value)} className="px-0" disabled={working}/>
      </div>
      <div className="flex flex-col gap-m p-m box-border" style={{width: '70%'}}>
        <h3>Appointment database</h3>
        <Grid items={appointments} className="flex-shrink-0">
          <GridColumn path="appointmentNumber" autoWidth header="#"/>
          <GridColumn path="firstName" autoWidth/>
          <GridColumn path="lastName" autoWidth/>
          <GridColumn path="date" autoWidth/>
          <GridColumn path="provider" autoWidth/>
          <GridColumn path="address" autoWidth/>
          <GridColumn path="billingStatus" autoWidth/>
          <GridColumn path="context" autoWidth/>
          <GridColumn path="appointmentStatus" autoWidth/>
        </Grid>
      </div>
    </SplitLayout>

  );
}