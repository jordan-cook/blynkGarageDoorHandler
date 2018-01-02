import groovy.transform.Field
@Field String blynkToken = "6d99b08cd42b4feb8f1131610afcbaa4"
@Field String pinToUse = "D24"

@Field String onURI = "http://blynk-cloud.com/" + blynkToken + "/update/" + pinToUse + "?value=1"
@Field String offURI = "http://blynk-cloud.com/" + blynkToken + "/update/" + pinToUse + "?value=0"

metadata {
    definition (name: "garageDoorHandler", namespace: "jordan-cook",
                author: "Jordan Cook") {
        capability "Switch"
    }
    tiles(scale: 2) {
        // standard tile with actions named
        standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
            state "off", label: '${currentValue}', action: "switch.on",
                  icon: "st.switches.switch.off", backgroundColor: "#ffffff", defaultState: true
            state "on", label: '${currentValue}', action: "switch.off",
                  icon: "st.switches.switch.on", backgroundColor: "#79b821"
        }


        // the "switch" tile will appear in the Things view
        main("switch")

        // the "switch" and "power" tiles will appear in the Device Details
        // view (order is left-to-right, top-to-bottom)
        details(["switch", "power"])
    }
}

def on() {
    def param1 = [
        uri: onURI,
        path: ""
    ]
    log.debug(param1)
    try {
        httpGet(param1) { resp1 ->
            sendEvent(name: "switch", value: "on")    
        }
        runIn(1, off)
    } catch (e) {
        log.error "something went wrong: $e"
    }   
}


def off() {
    def param2 = [
        uri: offURI
    ]
    try {
        httpGet(param2) { resp2 -> 
            sendEvent(name: "switch", value: "off")
        }     
    } catch (e) {
        log.error "something went wrong: $e"
    }  
}
