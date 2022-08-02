# voice-assistant-legion

Diese Projekt ist gedacht für Entwickler die Smart Home Geräte schnell und aufwandsarm an einen oder mehrere Sprachassistenten anbinden möchten

## Beschreibung

Das enthaltene Framework ist gedacht, um die "Sprache" von Sprachassistenten in einheitliche
Events umzuwandeln und vice versa. Dafür können Sprachassistenz-Module angelegt werden, die diese Aufgabe
übernehmen. Das wachsende Framework wird somit zunehmend mächtiger und bildet mehr und mehr Sprachassistenten
ab. Anwender des Frameworks müssen sich nicht mehr lange mit den API Seite der Sprachassistenten auseinandersetzen,
sondern können gleich los legen Smarte Geräte anzubinden.

Langfristig erhoffe ich mir eine Zusammenarbeit mit der Community um dieses Projekt weiter voranzutreiben.
Ich möchte Entwicklern ermöglichen in diesem Bereich einzusteigen und erhoffe mir, dass so die Hürde zum Mitmachen
gesenkt wird.

## Getting Started

In dieser Dokumentation wird von einem grundlegendem Verständis im Thema Smart Home und Sprachassistenten ausgegangen.
Anwender können diese Doku nutzen um das Projekt aufzusetzen und zu starten. Entwickler sollten sich vor der Weiterentwicklung
mit der API der jeweiligen Sprachassistenten auseinandergesetzt haben.

Damit das Framework tatsächlich mit Sprachassistenten kommunizieren kann müssen entsprechende Erweiterungen auf den jweiligen
Sprachassistenz-Anbieterseiten angelegt werden. 

Anleitungen finden sich hier:
* [Alexa Skill](https://developer.amazon.com/de-DE/alexa/alexa-skills-kit) 
* [Google Action](https://developers.google.com/assistant)

Im Rahmen der Einrichtung eines Skills oder Action wird auf das Thema Authentifizierungserver eingegangen. Weiter unten befinden sich eine kleiner 
Abschnitt zu diesem Thema.

### Dependencies

* Gradle 7.4.1
* OpenJDK 17
* Spring Boot 2.7.0

### Grundlendes

Das Framework beinhaltet zwei zentrale abstrakte Klassen: _**VoiceAssistant**_ und _**Infrastructure**_. Kommuniziert
wird über die Klasse **_Event_**. Erben der Klasse _**Event**_ bildet die Befehle ab die von Sprachassistenten geschickt
werden. Beispielsweise zum ein- und auschalten einer Glühbirne. Welche Befehle welchem _**Event**_ zuzuordnen werden
kann man weiter unten in der Tabelle ablesen. 

Beispiel: 

Das **PowerEvent** ist ein **Event** das zum ein- und ausschlaten von Geräten genutzt wird.

```java
public class PowerEvent extends Event{
  //Quellcode
}
```

### Kurzanleitung

Im Projekt muss eine Klasse hinzugefügt werden, die die abstrakte Klasse **_Infrastructure_** erbt. 

Beispiel:

Die Klasse **_CustomInfrastructure_** wird angelegt. Die Methode **_executeEvent_** dient dazu ankommende Events zu verarbeiten. Zwingend notwendig damit
das Framework mit allen Implementieren **_VoiceAssistant_** Klassen kommunizieren kann sind folgende Events:

* DiscoveryEvent - gibt eine Menge von Geräte zurück
* DeviceStateEvent - gibt den Status eines Geräts zurück
* StatusEvent - besteht aus einer Menge von DeviceStateEvents

Die ankommenden Events werden interpretiert und lösen anwenderabhängige Routinen aus. Hier ist es jedem frei überlassen, welche Logik er hinterlegen möchte.
Um Events mit den notwendigen Informationen zu füllen, werden die Setter-Methoden des ankommenden Events genutzt. Anschließend wird das selbe Event
zurückgeschickt.

Im Abschnitt Quellcode, des folgenden Beispiels, könnte die ankommenden Events mit einem Switch-Case Block interpretieren:
```java
public class CustomInfrastructure extends Infrastructure{

  public List<Event> executeEvent(Event event){
    //Quellcode
    return event;
  }
}
```

Anschließend muss noch eine Bean angelegt werden, die die erstellte **_CustomInfrastructure_** Klasse zurückgibt.
````java
@Bean
public Infrastructure infrastructure() {
    return new CustomInfrastructure();
  }
````

Ebenfalls müssen eine oder mehrere Schnittstellen angelegt werden damit das System erreichbar ist. Beispielsweise REST-Schnittstellen.
Von dort können die **_VoiceAssistants_** von der Klasse **_VoiceAssistantContext_** aufgerufen werden.

Bei Alexa und Google Assistant reichen REST-Schnittstellen aus. Zur initialen Einrichtung können die Strings **_token_** und **_refreshToken_** erst einmal vernachlässigt werden. 

>**Hinweis**
>Token und RefreshToken sind wichtige Variablen um die Sicherheits des Systems gewährleisten zu können

Beispiel Alexa:
````java
private VoiceAssistantContext context = new VoiceAssistantContext();

@PostMapping("/alexa")
public String alexa(HttpServletRequest request) {
    context.setVoiceAssistant(new Alexa());
    String token = "";
    String refreshToken = "";
    String jsonBody = RequestUtils.getJsonBody(request);
    return voiceAssistantContext.handleRequest(token, refreshToken, jsonBody);
}
````

Das Framework ist einsatzbereit.

## Anleitung für Entwickler

Dieser Abschnitt dreht sich vor allem um die aktive Weiterentwicklung des Frameworks.

### Neuen Sprachassistenten hinzufügen

Dafür muss eine Klasse angelegt werden, die von der abstrakten Klasse **_VoiceAssistant_** erbt. Die zwei Methoden sind einmal für das Auslesen eines Requests und die Rückübersetzung aus einem Event
in die "Sprache" des entsprechenden Sprachassistenten. Dafür ist es nötig sich mit den APIs der Assistenten auseinanderzusetzen. 

```java
public class CustomAssistent extends VoiceAssistant{

  public List<Event> mapRequestToEvents(String jsonBody){
    //Quellcode
  }

  public String mapEventsToResponseString(List<Event> responseEvent){
    //Quellcode
  }
}
```

Da sich die Sprachassistenten in der Menge an Informationen die je nach Event benötigt werden unterscheiden, werden zusätzlich noch Interfaces pro **_VoiceAssistant_** implementiert. Diese Interfaces 
werden genutzt um übersichtlich und nachvollziehbar notwendige Variablen in bestimmten Klassen zu markieren.

Am einfachsten lässt sich das am Beispiel der Klasse **_Device_** erklären.

````java
@Getter
@Setter
public class Device implements AlexaDevice, GoogleDevice {

  //AlexaDevice, GoogleDevice
  private String deviceId;
  private List<Category> categories;
  private List<Capability> capabilities;
  private String name;

  //AlexaDevice
  private String description;
  private String manufacturerName;

  //GoogleDevice
  private boolean willReportState;
}
````

>**Hinweis**
> Die Interfaces sollen für alle Events, States und dem Device pro **_VoiceAssistant_** angelegt werden.

#### Category und Capability

Bei einer **_Category_** handelt es sich um ein Enum, welches eine Kategorie wiederspiegelt, in die sich ein Gerät einordnent lässt. Diese Kategorien ergeben sich aus denen, die Sprachassistenten wie
[Alexa](https://developer.amazon.com/en-US/docs/alexa/device-apis/alexa-discovery.html#display-categories) und [Google Assistant](https://developers.google.com/assistant/smarthome/guides) anbieten.

Bei einer **_Capability_** handelt es sich ebenfalls um ein Enum, welches eine Fähigkeiten der Geräte wiederspiegelt. Diese Kategorien ergeben sich aus denen, die Sprachassistenten wie
[Alexa](https://developer.amazon.com/en-US/docs/alexa/device-apis/alexa-discovery.html#display-categories) und [Google Assistant](https://developers.google.com/assistant/smarthome/guides) anbieten.
Die Anzahl der Kategorien wächst proportional zur Menge der **_Events_** und der **_States_**. Jede **_Capability_** hat ein **_State_** und ein **_Event_** Pendant.

#### Neues Event hinzufügen

Ein neues Event sollte im optimal Fall aus einem Befehl entstehen den alle implementieren Sprachassistenten anbieten, andernfalls würde man für jeden Sprachdienst seperate Verwenden was den Sinn
des Frameworks verfehlen würde. Beispielsweise das **_PowerEvent_**. Sowohl Alexa als auch Google Assistant haben Befehle die im Aufbau ähnlich sind und vor allem ist das Ziel des Befehls der selbe.

Ein Event, welches für die Steuerung eines Geräts gedacht ist erbt von der Klasse **_DeviceStateEvent_**. Nach Ausführung eines Events ist es bei Sprachassistenten
üblich den Status der Übertragung und den aktuellen Status des Geräts abzufragen. Dafür muss ein neuer **_State_**. Wie vorhin erklärt müssen dafür in jedem Sprachassistenten Interfaces hinterlegt
werden, damit alle notwendigen Infos übertragen werden können.

Alle implementierten **_VoiceAssistants_** müssen erweitert werden. Auch hier müssen Interfaces für dieses Event in allen Sprachassistenz-Paketen hinzugefügt werden. Dann muss jeder 
Sprachassistent das Event interpretieren können. Damit ist gemeint, dass der ankommende Befehl aus der Sprachassistenten-Cloud in dieses Event umgewandelt und mit Daten gefüllt weitergeleitet
wird. Wird das Event zurückgegeben muss es wieder in die Sprachassistenz-Sprach übersetzt werden können.

Schussendlich muss noch ein neuer **_Capability_** Wert hinzugefügt werden, damit Geräten die Fähigkeit des neuen Events zugeordnet werden können.

### Authentifizierungserver

Dieser Server ist notwendig, um Sprachassistenten zu autorisieren auf die eigene Projekt Implementation zuzugreifen. Die eben vernachlässigten token und refreshToken resultate aus diesem
Authentifizierungsprozess. Alexa und Google Assistanten nutzen den oauth2 Standard und schicken bei Anfragen auf das System diese Variablen mit. Mit dem Thema muss sich zunächst
selbstständig auseinandergesetzt werden. Anleitungen finden sich sicherlich, aber ein TODO ist es eine eigene zu schreiben und/oder den Server im Framework zu integrieren.

Für einen schnellen Einstieg ohne eignene Authentifizierungsserver kann für lokale Anwendungen **_alexa login with amazon_** verwendet werden. 

Folgende Anleitung hilft beim Einrichten. Nach der Einrichtung bei Alexa können die selben URLs auch bei der Einrichtung für eine Google Action verwendet werden. 
[Anleitung](https://youtu.be/ppaTbaz8DpI?t=1040)

>**Warnung**
>Dieses Vorgehen wird nicht für eine längerfristige Nutzungen empfohlen, da so Sicherheitslücken enstehen.

## Contributing

Pull-Requests sind willkommen. Bei größeren Änderungen öffnen Sie bitte zuerst ein Issue oder eine Discussion, um zu besprechen, was Sie ändern möchten.

Bitte stellen Sie sicher, dass Sie die Tests entsprechend aktualisieren.

## Unterstütze Sprachassistenten und Eventmapping

Aktuell unterstützt werden:

* [Alexa](https://developer.amazon.com/en-US/docs/alexa/device-apis/alexa-interface-reference.html)
* [Google Assistant](https://developers.google.com/assistant/smarthome/overview)

Das Mapping bezieht sich auf die im Framework angelegten Events die, jeweils Befehle der Sprachassistenten abbilden. Die folgende 
Tabelle zeigt die Mappings auf. Nachzulesen auf den Entwicklerseiten.

| Sprachassistent -><br/>Events <br/> I<br/>v | Alexa                                                                                                  | Google Assistant                                                                                 |
|---------------------------------------------|--------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| DiscoveryEvent                              | [Alexa.Discovery](https://developer.amazon.com/en-US/docs/alexa/device-apis/alexa-discovery.html)      | [action.devices.SYNC](https://developers.google.com/assistant/smarthome/reference/intent/sync)   |
| StatusEvent                                 | [Alexa.ReportState](https://developer.amazon.com/en-US/docs/alexa/device-apis/alexa-statereport.html)       | [action.devices.QUERY](https://developers.google.com/assistant/smarthome/reference/intent/query) |
|                                             |                                                                                                        |                                                                                                  |
| PowerEvent                                  | [Alexa.PowerController](https://developer.amazon.com/en-US/docs/alexa/device-apis/alexa-powercontroller.html) | [OnOff](https://developers.google.com/assistant/smarthome/traits/onoff)                          |

## License

[BSD 3](https://choosealicense.com/licenses/bsd-3-clause/)