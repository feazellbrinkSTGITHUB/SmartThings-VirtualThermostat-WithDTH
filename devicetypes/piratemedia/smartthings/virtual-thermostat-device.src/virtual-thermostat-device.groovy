metadata {
	definition (name: "Virtual Thermostat",
    namespace: "piratemedia/smartthings",
    author: "Eliot S.",
    mnmn: "SmartThings", 
    vid: "generic-thermostat-1",
    executeCommandsLocally: true,
    ocfDeviceType: "oic.d.thermostat") {
		capability "Temperature Measurement"
		capability "Thermostat"
		capability "Thermostat Mode"
		capability "Thermostat Heating Setpoint"
		capability "Thermostat Cooling Setpoint"
		capability "Thermostat Operating State"
		capability "Configuration"
		capability "Refresh"

		command "refresh"
		command "poll"
        
		command "offbtn"
		command "heatbtn"
		command "coolbtn"
		command "levelUpDown"
		command "levelUp"
		command "levelDown"
		command "heatingSetpointUp"
		command "coolingSetpointUp"
		command "heatingSetpointDown"
		command "coolingSetpointDown"
		command "changeMode"
		command "setVirtualTemperature", ["number"]
		command "setHeatingStatus", ["string"]
        
		attribute "temperatureUnit", "string"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {
		multiAttributeTile(name:"temperature", type:"thermostat", width:6, height:4, canChangeIcon: true) {
			tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
				attributeState("default", label:'${currentValue}°', unit: unitString())
			}
            
			tileAttribute("device.thermostatSetpoint", key: "VALUE_CONTROL") {
				attributeState("default", action: "levelUpDown")
				attributeState("VALUE_UP", action: "levelUp")
				attributeState("VALUE_DOWN", action: "levelDown")
			}
            
			tileAttribute("device.thermostatOperatingState", key: "OPERATING_STATE") {
				attributeState("idle",		    backgroundColor: "#44B621")
				attributeState("heating",	    backgroundColor: "#FFA81E")
				attributeState("cooling",	    backgroundColor: "#1EF8FF")
				attributeState("off",		    backgroundColor: "#ddcccc")
				attributeState("pending heat",	backgroundColor: "#e60000")
				attributeState("pending cool",	backgroundColor: "#000FE6")
			}
            
			tileAttribute("device.thermostatMode", key: "THERMOSTAT_MODE") {
				attributeState("off", label:'Off')
				attributeState("heat", label:'Heat')
				attributeState("cool", label:'Cool')
			}
            
			tileAttribute("device.thermostatSetpoint", key: "HEATING_SETPOINT") {
				attributeState("default", label:'${currentValue}')
			}
            
			tileAttribute("device.thermostatSetpoint", key: "COOLING_SETPOINT") {
				attributeState("default", label:'${currentValue}')
			}

		}
        
		valueTile("temp2", "device.temperature", width: 2, height: 2, decoration: "flat") {
			state("default", label:'${currentValue}°', icon:"https://raw.githubusercontent.com/racarmichael/SmartThings-VirtualThermostat-WithDTH/master/images/device.png",
					backgroundColors: getTempColors(), canChangeIcon: true)
		}
        
		standardTile("thermostatMode", "device.thermostatMode", width:2, height:2, decoration: "flat") {
			state("heat", 	action:"changeMode", nextState: "updating", icon: "https://raw.githubusercontent.com/racarmichael/SmartThings-VirtualThermostat-WithDTH/master/images/heat_icon.png")
			state("cool", 	action:"changeMode", nextState: "updating", icon: "https://raw.githubusercontent.com/racarmichael/SmartThings-VirtualThermostat-WithDTH/master/images/cool_icon.png")
			state("off", 	action:"changeMode", nextState: "updating", icon: "https://raw.githubusercontent.com/racarmichael/SmartThings-VirtualThermostat-WithDTH/master/images/off_icon.png")
			state("Updating", label:"", icon: "https://raw.githubusercontent.com/racarmichael/SmartThings-VirtualThermostat-WithDTH/master/images/cmd_working.png")
		}
        
		standardTile("heatBtn", "device.canHeat", width:1, height:1, decoration: "flat") {
			state("Heat", action: "heatbtn", icon: "https://raw.githubusercontent.com/racarmichael/SmartThings-VirtualThermostat-WithDTH/master/images/heat_icon.png")
			state "false", label: ''
		}
        
		standardTile("coolBtn", "device.canCool", width:1, height:1, decoration: "flat") {
			state("Cool", action: "coolbtn", icon: "https://raw.githubusercontent.com/racarmichael/SmartThings-VirtualThermostat-WithDTH/master/images/cool_icon.png")
			state "false", label: ''
		}
        
		standardTile("offBtn", "device.off", width:1, height:2, decoration: "flat") {
			state("Off", action: "offbtn", icon: "https://raw.githubusercontent.com/racarmichael/SmartThings-VirtualThermostat-WithDTH/master/images/off_icon.png")
		}
        
		standardTile("refresh", "device.refresh", width:1, height:2, decoration: "flat") {
			state "Refresh", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        
		valueTile("heatingSetpoint", "device.thermostatSetpoint", width: 1, height: 1) {
			state("heatingSetpoint", label:'${currentValue}', unit: unitString(), foregroundColor: "#FFFFFF",
				backgroundColors: [ [value: 0, color: "#FFFFFF"], [value: 7, color: "#FF3300"], [value: 15, color: "#FF3300"] ])
			state("disabled", label: '', foregroundColor: "#FFFFFF", backgroundColor: "#FFFFFF")
		}
        
		standardTile("heatingSetpointUp", "device.thermostatSetpoint", width: 1, height: 1, canChangeIcon: true, decoration: "flat") {
			state "default", label: '', action:"heatingSetpointUp", icon:"https://raw.githubusercontent.com/racarmichael/SmartThings-VirtualThermostat-WithDTH/master/images/heat_arrow_up.png"
			state "", label: ''
		}
        
		standardTile("heatingSetpointDown", "device.thermostatSetpoint",  width: 1, height: 1, canChangeIcon: true, decoration: "flat") {
			state "default", label:'', action:"heatingSetpointDown", icon:"https://raw.githubusercontent.com/racarmichael/SmartThings-VirtualThermostat-WithDTH/master/images/heat_arrow_down.png"
			state "", label: ''
		}
        
		controlTile("heatSliderControl", "device.thermostatSetpoint", "slider", height: 1, width: 4, range: getRange(), inactiveLabel: false) {
			state "default", action:"setHeatingSetpoint", backgroundColor:"#FF3300"
			state "", label: ''
		}
        
		valueTile("coolingSetpoint", "device.thermostatSetpoint", width: 1, height: 1) {
			state("coolingSetpoint", label:'${currentValue}', unit: unitString(), foregroundColor: "#FFFFFF",
				backgroundColors: [ [value: 0, color: "#FFFFFF"], [value: 7, color: "#0022FF"], [value: 15, color: "#0022FF"] ])
			state("disabled", label: '', foregroundColor: "#FFFFFF", backgroundColor: "#FFFFFF")
		}
        
		standardTile("coolingSetpointUp", "device.thermostatSetpoint", width: 1, height: 1, canChangeIcon: true, decoration: "flat") {
			state "default", label: '', action:"coolingSetpointUp", icon:"https://raw.githubusercontent.com/racarmichael/SmartThings-VirtualThermostat-WithDTH/master/images/cool_arrow_up.png"
			state "", label: ''
		}
        
		standardTile("coolingSetpointDown", "device.thermostatSetpoint",  width: 1, height: 1, canChangeIcon: true, decoration: "flat") {
			state "default", label:'', action:"coolingSetpointDown", icon:"https://raw.githubusercontent.com/racarmichael/SmartThings-VirtualThermostat-WithDTH/master/images/cool_arrow_down.png"
			state "", label: ''
		}
        
		controlTile("coolSliderControl", "device.thermostatSetpoint", "slider", height: 1, width: 4, range: getRange(), inactiveLabel: false) {
			state "default", action:"setCoolingSetpoint", backgroundColor:"#0022ff"
			state "", label: ''
		}

		main("temp2")
        
		details( ["temperature", "thermostatMode",
				"heatingSetpointDown", "heatingSetpoint", "heatingSetpointUp", "heatBtn",
				"heatSliderControl", 
				"refresh", "offBtn",
				"coolingSetpointDown", "coolingSetpoint", "coolingSetpointUp","coolBtn",
				"coolSliderControl"] )
	}
}

def shouldReportInCentigrade() {
	try {
    	def ts = getTemperatureScale();
    	return ts == "C"
    } catch (e) {
    	log.error e
    }
    return true;
}

def installed() {
    log.trace "Executing 'installed'"
    initialize()
}

def configure() {
    log.trace "Executing 'configure'"
    initialize()
}

private initialize() {
    log.trace "Executing 'initialize'"
    
    setHeatingSetpoint(defaultTemp())
    setCoolingSetpoint(defaultTemp())
    setVirtualTemperature(defaultTemp())
    setHeatingStatus("off")
    setCoolingStatus("off")
    setThermostatMode("off")
    sendEvent(name:"supportedThermostatModes",    value: ['heat', 'cool', 'off'], displayed: false)
    sendEvent(name:"supportedThermostatFanModes", values: [], displayed: false)
    
	state.tempScale = "C"
}

def getTempColors() {
	def colorMap
        //getTemperatureScale() == "C"   wantMetric()
	if(shouldReportInCentigrade()) {
		colorMap = [
			// Celsius Color Range
			[value: 0, color: "#153591"],
			[value: 7, color: "#1e9cbb"],
			[value: 15, color: "#90d2a7"],
			[value: 23, color: "#44b621"],
			[value: 29, color: "#f1d801"],
			[value: 33, color: "#d04e00"],
			[value: 36, color: "#bc2323"]
			]
	} else {
		colorMap = [
			// Fahrenheit Color Range
			[value: 40, color: "#153591"],
			[value: 44, color: "#1e9cbb"],
			[value: 59, color: "#90d2a7"],
			[value: 74, color: "#44b621"],
			[value: 84, color: "#f1d801"],
			[value: 92, color: "#d04e00"],
			[value: 96, color: "#bc2323"]
		]
	}
}

def unitString() {  return shouldReportInCentigrade() ? "C": "F" }
def defaultTemp() { return shouldReportInCentigrade() ? 45 : 70 }
def lowRange() { return shouldReportInCentigrade() ? 9 : 45 }
def highRange() { return shouldReportInCentigrade() ? 45 : 113 }
def getRange() { return "${lowRange()}..${highRange()}" }

def getTemperature() {
	return device.currentValue("temperature")
}

def setHeatingSetpoint(temp) {
	def ctsp = device.currentValue("thermostatSetpoint");
    def chsp = device.currentValue("heatingSetpoint");

    if(ctsp != temp || chsp != temp) {
        sendEvent(name:"thermostatSetpoint", value: temp, unit: unitString(), displayed: false)
        sendEvent(name:"heatingSetpoint", value: temp, unit: unitString())
    }
}

def heatingSetpointUp() {
	def hsp = device.currentValue("thermostatSetpoint")
	if(hsp + 1.0 > highRange()) return;
	setHeatingSetpoint(hsp + 1.0)
}

def heatingSetpointDown() {
	def hsp = device.currentValue("thermostatSetpoint")
	if(hsp - 1.0 < lowRange()) return;
	setHeatingSetpoint(hsp - 1.0)
}

def setCoolingSetpoint(temp) {
	def ctsp = device.currentValue("thermostatSetpoint");
    def ccsp = device.currentValue("coolingSetpoint");

    if(ctsp != temp || ccsp != temp) {
        sendEvent(name:"thermostatSetpoint", value: temp, unit: unitString(), displayed: false)
        sendEvent(name:"coolingSetpoint", value: temp, unit: unitString())
    }
}

def coolingSetpointUp() {
	def csp = device.currentValue("thermostatSetpoint")
	if(csp + 1.0 > highRange()) return;
	setCoolingSetpoint(csp + 1.0)
}

def coolingSetpointDown() {
	def csp = device.currentValue("thermostatSetpoint")
	if(csp - 1.0 < lowRange()) return;
	setCoolingSetpoint(csp - 1.0)
}

def levelUp() {
	def hsp = device.currentValue("thermostatSetpoint")
	if(hsp + 1.0 > highRange()) return;
    setHeatingSetpoint(hsp + 1.0)
}

def levelDown() {
    def hsp = device.currentValue("thermostatSetpoint")
	if(hsp - 1.0 < lowRange()) return;
    setHeatingSetpoint(hsp - 1.0)
}

def parse(data) {
    log.debug "parse data: $data"
}

def refresh() {
    log.trace "Executing refresh"
    sendEvent(name: "supportedThermostatModes",    value: ['heat', 'cool', 'off'], displayed: false)
    sendEvent(name: "supportedThermostatFanModes", values: [], displayed: false)
}

def getThermostatMode() {
	return device.currentValue("thermostatMode")
}

def getOperatingState() {
	return device.currentValue("thermostatOperatingState")
}

def getThermostatSetpoint() {
	return device.currentValue("thermostatSetpoint")
}

def getHeatingSetpoint() {
	return device.currentValue("heatingSetpoint")
}

def getCoolingSetpoint() {
	return device.currentValue("coolingSetpoint")
}

def poll() {
}

def offbtn() {
	setThermostatMode("off")
}

def heatbtn() {
	setThermostatMode("heat")
}

def coolbtn() {
	setThermostatMode("cool")
}

def setThermostatMode(mode) {
	if(device.currentValue("thermostatMode") != mode) {
    	sendEvent(name: "thermostatMode", value: mode)
    }
}

def levelUpDown() {
}

def changeMode() {
	def val = device.currentValue("thermostatMode") == "off" ? "heat" : "off"
	setThermostatMode(val)
    return val
}

def setVirtualTemperature(temp) {
	sendEvent(name:"temperature", value: temp, unit: unitString(), displayed: true)
}

def setHeatingStatus(string) {
	if(device.currentValue("thermostatOperatingState") != string) {
		sendEvent(name:"thermostatOperatingState", value: string)
    }
}
def setCoolingStatus(string) {
	if(device.currentValue("thermostatOperatingState") != string) {
		sendEvent(name:"thermostatOperatingState", value: string)
    }
}
