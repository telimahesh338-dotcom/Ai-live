// Inside processQuery or result handler in VoiceAssistant.tsx
const handleAIResponse = (text: string) => {
    // If AI says: "WHATSAPP:919876543210|Hello darling"
    if (text.includes("WHATSAPP:")) {
        const parts = text.split("WHATSAPP:")[1].split("|");
        const phone = parts[0];
        const msg = parts[1];
        
        // Trigger the Android Native Bridge
        if (window.AndroidAgent) {
            window.AndroidAgent.sendWhatsApp(phone, msg);
        }
    }
};