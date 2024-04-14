---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# ClientCare

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project was based on the [AddressBook-Level3 (AB3](https://se-education.org/addressbook-level3/) from [SE-EDU](https://se-education.org/).
* The `remark` command in this project was implemented with reference to the CS2103T AB3 [Tutorial: Adding a command](https://nus-cs2103-ay2324s2.github.io/tp/tutorials/AddRemark.html).
* The user guide approach in breakdown in sections and header styling was inspired by [ArtBuddy](https://ay2223s1-cs2103t-w11-3.github.io/tp/UserGuide.html)
--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2324S2-CS2103T-W12-1/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2324S2-CS2103T-W12-1/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2324S2-CS2103T-W12-1/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `ClientListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2324S2-CS2103T-W12-1/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2324S2-CS2103T-W12-1/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2324S2-CS2103T-W12-1/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2324S2-CS2103T-W12-1/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2324S2-CS2103T-W12-1/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.


### View Client feature

The `view` command allows users to view the details of a client on the GUI using their `INDEX`. Other commands will also affect the client display in the GUI. This includes information not included in the client list cards, such as their last met and policy list.

#### Implementation

The view client mechanism is facilitated by `DisplayClient` in the model. There are three possible outcomes when a command is executed:
1. When a command adding a client (e.g. `add`) or referring to a client using `INDEX` (e.g. `view`, `met`,`deletepolicy`) is executed, the `DisplayClient` is set to the client that was operated on. 
2. When a command that changes the client list is executed (e.g. `list`, `sort`, `find`), the `DisplayClient` is set to the first client in the new list.
3. When `delete` or `clear` is executed, the `DisplayClient` is set to `null`. 
This is done with the `setDisplayClient()` function in the `Model`, that is also implemented in `Logic`.

The sequence diagram below shows the execution of `view 1` to view the details of client at index 1.
<puml src="diagrams/ViewClientSequenceDiagram.puml" width="900" />

`MainWindow` handles most of the UI logic in regard to displaying the viewed client on the GUI, including refreshing the `ClientDetailsPanel` and `ClientPolicyTable`. It also sets `DisplayClient` on startup to the first client in the list when there is at least one client, otherwise it sets `DisplayClient` to `null`.

#### Design Considerations

**Aspect: Where `DisplayClient` should be stored or handled.**

* Current: `DisplayClient` is stored and handled in `Model`, with relevant functions in `Logic`.
  * The functions in `Model` and `Logic` are similar to those for `AddressBook` to get and set the `DisplayClient`. 
  * It follows the current design, where the viewable `ObservableList<Person>` for the GUI is separate from the `AddressBook`.
  * This allows for `DisplayClient` to be used even when there is no command executed. For example, when the application starts up, it displays the first client in the list, despite no command execution.
* Alternative (not taken): `DisplayClient` is handled in `CommandResult`.
  * In this implementation, `DisplayClient` would simply be a `Person` set by `CommandResult`, similar to the current `feedbackToUser` implementation.
  * However, this means `DisplayClient` can only be set after a command, which does not allow us to set `DisplayClient` on application startup.


### Adding notes to client feature

The `remark` command allows users to add an optional note to a client.

#### Implementation

The `remark` command was implemented according to [Tutorial: Adding a command](https://nus-cs2103-ay2324s2.github.io/tp/tutorials/AddRemark.html) from CS2103T and AB3. The mechanism is similar to `edit`, and is facilitated by `RemarkCommand`. It creates and returns a `personToEdit`, where only the `Remark` is updated to the new remark entered by the user. Changes are made using `Model#setPerson(Person, Person)`.

#### Design Considerations

**Aspect: Behaviour of `r/` prefix if more than one is used.

* Current: Only the last one is used to update `Remark`.
  * For example, `remark 1 r/typo r/corrected` will only capture `corrected`.
  * This is to allow users to quickly correct typos by simply typing a new remark rather than move their cursor and backspace to fix the typo.
* Alternative (not taken): Disallow duplicate `r/` prefixes.
  * The example above will give the user a command error instead.
  * This might cause minor convenience to users since they will have to ensure they do not use `r/` in their remarks. It also means fixing typos will be slower, like mentioned above.
* Alternative (not taken): Remove `r/` prefix.
  * With this, the command format will be `remark INDEX REMARK` instead.
  * While this might make it easier to type, it will also make fixing typos slower, like mentioned in the current behaviour.
  * It also means that a separate way of parsing has to be used, instead of `ArgumentMultimap`, deviating from other commands.


### Sorting clients feature

The `sort` command allows users to sort the client list by a specified `sort criteria` that can be `name`, `priority` or `birthday`, and a `sort order` that can be `asc` or `desc`.

#### Implementation

The functionality to `sort` clients is implemented in the `SortCommand` class. The `SortCommandParser` class is responsible for parsing the user input and creating a `SortCommand` object.

The `SortCommandParser` class parses the input arguments by storing the prefixes of their respective values in a `ArgumentMultimap` object, and create a new `SortCommand` object with the parsed `SortCriteria` and `SortOrder`.

The `SortCommand` object does the following:
- `PersonComparator#getComparator(SortCriteria, SortOrder)` is used to get the `Comparator<Person>` object using the `SortCriteria` and `SortOrder`.
- `Model#updateSortPersonComparator(Comparator<Person>)` - Updates the `Comparator<Person>` object used to sort the list of persons in the `Model` component.
- `Model#setDisplayClientAsFirstInSortedFilteredPersonList()` - Updates the displayed client in the UI to the first client in the sorted list of persons.

The following object diagram illustrates the above:
<puml src="diagrams/SortPersonsObjectDiagram.puml" width="600" />

The following sequence diagram shows the `sort` operation:
<puml src="diagrams/SortPersonsSequenceDiagram.puml" width="900" />

#### Design Considerations

In order to keep `ModelManager#filteredPersons` as an immutable `final` field, we have decided not to modify the `filteredPersons` directly. Instead, we do the following:
- we store the `Comparator<Person>` object in `ModelManager#personComparator`, which can be updated by `ModelManager#updateSortPersonComparator(Comparator<Person>)`.
- When a sorted list of persons is needed, we call `ModelManager#getSortedFilteredPersonList()` which returns a new sorted list of persons sorted using the `ModelManager#personComparator`.

This way, the original order of `ModelManager#filteredPersons` is preserved, and we can get a sorted list of persons when needed.


### Updating last met feature
The last met feature allows users to keep track and update their last interaction with their clients.

#### Implementation

The updating of last met command is facilitated by the `LastMetCommandParser` class which is created by the `AddressBookParser`.

The `LastMetCommandParser#parse()` overrides `Parser#parse()` in the `Parser` interface.
- `LastMetCommandParser#parse()` - Parses the input arguments by storing the prefixes of it respective values in a `ArgumentMultimap` object.
- It will then convert the String input into a Date object before creating a new `LastMetCommand` object with the formatted date.

The `LastMetCommand` object is then executed by the `Logic` component.

The `LastMetCommand` object then communicates with the `Model` component to update the LastMet to the client. The `Model` component then updates the `Person` object with the new LastMet.
- `Model#setPerson(Person, Person)` - Sets the client in the existing client list to the new `Person` object which has been edited by the `LastMetCommand#execute()` which contains the new LastMet.
- `Model#setDisplayClient(Person)` - Updates the displayed client in the UI to the client that has been edited with the new LastMet.

The method `LastMetCommand#execute()` returns a CommandResult object which contains the success message to be displayed to the user.

The following object diagram illustrates the above:
<puml src="diagrams/LastMetObjectDiagram.puml" width="600" />

The following sequence diagram shows the lastmet operation:
<puml src="diagrams/LastMetSequenceDiagram.puml" width="900" />


### Setting last met overdue duration feature
The setting last met overdue duration feature allows users to choose the number of days before clients show up in the Last Met Display.

#### Implementation

The updating of last met command is facilitated by the `SetCommandParser` class which is created by the `AddressBookParser`.

The `SetCommandParser#parse()` overrides `Parser#parse()` in the `Parser` interface.
- `SetCommandParser#parse()` - Parses the input arguments by storing the prefixes of it respective values in a `ArgumentMultimap` object.
- It will then convert the String input into an Integer object before creating a new `SetCommand` object with the formatted integer.

The `SetCommand` object is then executed by the `Logic` component.

The `SetCommand` object then communicates with the `Model` component to update the static lastMetDuration value in the Last Met class.
Upon updating the new lastMetDuration value, the `SetCommand` object then calls the `checkOverdue` function in all the `Person` objects in the addressbook
to update the `isOverdue` boolean in all the `Person` objects. The Last Met Display then shows all the updated `Person` objects with `isOverdue` equals to `true`.

The method `SetCommand#execute()` returns a CommandResult object which contains the success message to be displayed to the user.


### Adding schedule feature
The adding schedule feature allows users to make appointments to track all their upcoming appointments with clients.

#### Implementation

The updating of schedule command is facilitated by the `ScheduleCommandParser` class which is created by the `AddressBookParser`.

The `ScheduleCommandParser#parse()` overrides `Parser#parse()` in the `Parser` interface.
- `ScheduleCommandParser#parse()` - Parses the input arguments by storing the prefixes of it respective values in a `ArgumentMultimap` object.
- It will then convert the String input into a DateTime object before creating a new `ScheduleCommand` object with the formatted dateTime.

The `ScheduleCommand` object is then executed by the `Logic` component.

The `ScheduleCommand` object then communicates with the `Model` component to update the Schedule to the client. The `Model` component then updates the `Person` object with the new Schedule.
- `Model#setPerson(Person, Person)` - Sets the client in the existing client list to the new `Person` object which has been edited by the `ScheduleCommand#execute()` which contains the new Schedule.
- `Model#setDisplayClient(Person)` - Updates the displayed client in the UI to the client that has been edited with the new Schedule.

The method `ScheduleCommand#execute()` returns a CommandResult object which contains the success message to be displayed to the user.


### Marking schedule feature
The marking schedule feature allows users to close upcoming appointments to track all their upcoming appointments with clients.

#### Implementation

The marking of schedule command is facilitated by the `MarkCommandParser` class which is created by the `AddressBookParser`.

The `MarkCommandParser#parse()` overrides `Parser#parse()` in the `Parser` interface.
- `MarkCommandParser#parse()` - Parses the input arguments by storing the prefixes of it respective values in a `ArgumentMultimap` object.
- It will then convert the String input into a DateTime object before creating a new `MarkCommand` object with the formatted dateTime.

The `MarkCommand` object is then executed by the `Logic` component.

The `MarkCommand` object then communicates with the `Model` component to update the Schedule to the client. The `Model` component then updates the `Person` object with the new Schedule with `isDone` set to `true`.
- `Model#setPerson(Person, Person)` - Sets the client in the existing client list to the new `Person` object which has been edited by the `MarkCommand#execute()` which contains the new Schedule.
- `Model#setDisplayClient(Person)` - Updates the displayed client in the UI to the client that has been edited with the new Schedule.

The method `MarkCommand#execute()` returns a CommandResult object which contains the success message to be displayed to the user.


### Add policy feature
The add policy feature allows users to add a policy to a client. The policy is stored in the `Policy` class, which contains the policy details such as policy name, policy id. The `Policy` class is then added to the `PolicyList` object stored within the `Person` object in the `Model` component.

#### Implementation

The add policy command mechanism is facilitated by the `AddPolicyCommandParser` class which is created by the `AddressBookParser`.

The `AddPolicyCommandParser` class is responsible for parsing the user input and creating an `AddPolicyCommand` object.

The `AddPolicyCommandParser#parse()` overrides `Parser#parse()` in the `Parser` interface.
- `AddPolicyCommandParser#parse()` - Parses the input arguments by storing the prefixes of it respective values in a `ArgumentMultimap` object, and creates a new `AddPolicyCommand` object with the parsed policy name and policy ID.

The `AddPolicyCommand` object is then executed by the `Logic` component.

The `AddPolicyCommand` object then communicates with the `Model` component to add the policy to the client. The `Model` component then updates the `Person` object with the new policy.
- `Model#setPerson(Person, Person)` - Sets the client in the existing client list to the new `Person` object which has been edited by the `AddPolicyCommand#execute()` which contains the new policy.
- `Model#setDisplayClient(Person)` - Updates the displayed client in the UI to the client that has been edited with the new policy.

The method `AddPolicyCommand#execute()` returns a CommandResult object which contains the success message to be displayed to the user.

The following object diagram illustrates the above:
<puml src="diagrams/AddPolicyObjectDiagram.puml" width="600" />

The following sequence diagram shows the addpolicy operation:
<puml src="diagrams/AddPolicySequenceDiagram.puml" width="900" />


### Deleting policy feature


### Extensions to add command and edit command: Add birthday, edit birthday, add priority, edit priority features

The add birthday and edit birthday features allow users to add and edit the birthday of a client. Birthdays support the birthday reminders feature. The birthday is stored in the `Birthday` class, which contains the birthday details such as day, month, and year. The `Birthday` class is part of the `Person` object in the `Model` component.

The add priority and edit priority features allow users to add and edit the priority of a client. Priority supports the sort by priority feature, and helps optimise client management. The priority is stored in the `Priority` class, which contains the priority details such as priority value. The priority value are enumerated, and can be one of the following: `LOW`, `MEDIUM`, `HIGH`, `VIP`. The `Priority` class is part of the `Person` object in the `Model` component.

#### Implementation

The functionality to add and edit birthday and priority is implemented in the `AddCommand` and `EditCommand` classes. The `AddCommandParser` and `EditCommandParser` classes are responsible for parsing the user input and creating an `AddCommand` or `EditCommand` object respectively.

The `AddCommandParser` and `EditCommandParser` classes parse the input arguments by storing the prefixes of their respective values in a `ArgumentMultimap` object, and create a new `AddCommand` or `EditCommand` object with the parsed birthday or priority, amongst other fields.

The `AddCommand` and `EditCommand` objects then communicate with the `Model` component to add or edit the birthday or priority of the client. The `Model` component then adds or edits the `Person` object with the new birthday or priority, amongst other fields.

The `AddCommand` object then communicates with the `Model` component to add a person.
- `Model#addPerson(Person)` - Adds the new client to the existing client list.
- `Model#setDisplayClient(Person)` - Updates the displayed client in the UI to the client that has been added.

The following object diagram illustrates the above:
<puml src="diagrams/AddPersonObjectDiagram.puml" width="600" />

The following sequence diagram shows the `add` operation:
<puml src="diagrams/AddPersonSequenceDiagram.puml" width="900" />

More on `Birthday` class
* Birthday is immutable and stores the day, month and year as a `LocalDate` object, as time is not relevant for birthday.
* The message constraints for birthday utilise the `DateUtil` common class to ensure that the date is valid and in the correct format.
* `DateUtil` class is used to validate (conforms to `DateUtil` date format and is parsable) and parse the string to a `LocalDate` object. `DateUtil` is also used to ensure that the date is not in the future.
* Refer to the `DateUtil` class for more information on the date format and parsing.

More on `Priority` class
* Priority is immutable and stores the priority value as a `PriorityValue` object, which is an enumerated type, to ensure that priority value is a valid type.
* The message constraints for priority utilise the `PriorityValue` enum class which should be responsible for the `toString()` logic for display.
* `PriorityValue` enum class is used to validate the priority value, which is responsible for the possible valid priority values.
* Refer to the `PriorityValue` enum class for more information on the priority values.

More on `PriorityValue` enum class
* `PriorityValue` is an enumerated type that contains the possible valid priority values: `LOW`, `MEDIUM`, `HIGH`, `VIP`.
* When parsing from a string and displaying as a string, the `PriorityValue` allows full form values (`low`, `medium`, `high`, `vip`) and short form values (`l`, `m`, `h`, `v`) to be used interchangeably.
* Parsing from a string to a `PriorityValue` object is case-insensitive, and is handled by `getPriority`.
* Obtaining the all available full form and short form of the `PriorityValue` object is handled by `getFullPriorities()` and `getShortPriorities()` respectively.
* The mapping of the full form strings and short form strings to the enum values is handled through `HashMap<String, PriorityValue> FULL_PRIORITY_MAP` and `HashMap<String, PriorityValue> SHORT_PRIORITY_MAP`, which has a constant time complexity.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* insurance agents
* has a need to manage a significant number of clients for insurance policies
* has a need to organise schedules with clients and their details in one place
* has a need for reminders to keep in touch with clients
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:

* conveniently manage client details and schedules faster than a typical mouse/GUI driven app
* Convenient tracking of when agent last checked up on clients (eg. reminders)
* Organise client contacts details
* Optimization by client’s importance (VIP status etc)
* Monitor client’s insurance policies
* Scheduler to manage appointment to ensure timely follow-up

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​         | I want to …​                                          | So that I can…​                                                       |
|----------|-----------------|-------------------------------------------------------|-----------------------------------------------------------------------|
| `* * *`  | insurance agent | see usage instructions                                | refer to instructions when I forget how to use the App                |
| `* * *`  | insurance agent | add a new client contact details                      | keep track of the clients I have                                      |
| `* * *`  | insurance agent | delete a client                                       | remove clients that are leaving                                       |
| `* * *`  | insurance agent | find a client by name                                 | locate details of client without having to go through the entire list |
| `* * *`  | insurance agent | list all clients                                      | see all clients at a glance                                           |
| `* * *`  | insurance agent | view client information                               | know and check client details                                         |
| `* *`    | insurance agent | check schedules with clients on a date                | keep track of what I have to do in a day                              |
| `* *`    | insurance agent | add the birthday of my clients                        | wish them happy birthday to keep in contact with them                 |
| `* *`    | insurance agent | delete policy details for a client                    | remove expired policies of the client                                 |
| `* *`    | insurance agent | see when I last met a client                          | check in on a client that I have not met for a long time              |
| `* *`    | insurance agent | mark that a schedule is completed                     | know that i fulfilled the appointment scheduled                       |
| `* *`    | insurance agent | add policy details of a client                        | keep track of clients and their policies                              |
| `* *`    | insurance agent | schedule checkup date and time for clients            | know when to follow-up with them                                      |
| `*`      | insurance agent | set the overdue period for last met                   | be reminded of a follow-up at my own pace                             |
| `*`      | insurance agent | sort clients by priority                              | deal with client with higher priority status first                    |
| `*`      | insurance agent | track deals that I have closed                        | track my current progress                                             |
| `*`      | insurance agent | edit my client's details                              | update my client's details                                            |
| `*`      | insurance agent | reschedule my appointments                            | change the date and time of appointments with clients                 |
| `*`      | insurance agent | filter clients by importance                          | decide on who to prioritise on                                        |
| `*`      | insurance agent | get help                                              | use the app when I am lost or confused                                |
| `*`      | insurance agent | can sort clients by the expected revenue of the deals | know which clients to prioritise                                      |
| `*`      | insurance agent | set the policy payment due dates                      | remind my clients.                                                    |
| `*`      | insurance agent | add the maturity date of my client’s policy           | update them and plan for future policies                              |
| `*`      | developer       | view list of all bugs reported by users               | conveniently view all reported bugs and fix them                      |
| `*`      | colleague       | import someone's contact list                         | take over his clients                                                 |
| `*`      | insurance agent | report issues/bugs                                    | get someone to fix bugs                                               |
| `*`      | developer	      | get a log list of user activity                       | view user activity to bug fix                                         |
| `*`      | manager         | view all my subordinates' clients                     | be aware of their progress and client base                            |
| `*`      | insurance agent | get reminders of client birthday                      | send birthday message                                                 |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `ClientCare` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - Add new client**

**MSS**
1.  User requests to add new client.
2.  ClientCare adds the new client to the list.
3.  ClientCare shows the client's details and success message to the user.<br>
    Use case ends.

**Extensions**
* 1a. ClientCare detects that the given command is invalid.
  * 1a1. ClientCare shows an error message.
  * 1a2. ClientCare requests for the correct input.
  * 1a3. User enters new data.<br>
    Steps 1a1-1a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 2.
* 1b. ClientCare detects that the client to be added  already exists in the system.
  * 1b1. ClientCare shows an error message.<br>
    Use case ends.


**Use case: UC02 - Delete client**

**MSS**
1.  User requests to <u>list all clients (UC04)</u> or <u>find client by name (UC06)</u>.
2.  ClientCare shows a list of clients.
3.  User requests to delete a specific client in the list by index.
4.  ClientCare deletes the client.<br>
5.  ClientCare shows a success message to the user.<br>
    Use case ends.

**Extensions**
* 2a. User sees that the list is empty.
  * 2a1. User stops as there is no client to delete.
    Use case ends.
* 3a. ClientCare detects that the given command is invalid.
  * 3a1. ClientCare shows an error message.
  * 3a2. ClientCare requests for the correct input.
  * 3a3. User enters new data.<br>
    Steps 3a1-3a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 4.
* 3b. ClientCare detects that the client does not exist.
  * 3b1. ClientCare shows an error message.<br>
    Use case ends.


**Use case: UC03 - Edit client**

**MSS**
1.  User requests to <u>list all clients (UC04)</u> or <u>find client by name (UC06)</u>.
2.  ClientCare shows a list of clients.
3.  User requests to edit a specific client in the list by index.
4.  ClientCare edits the client.<br>
5.  ClientCare shows the client's details and success message to the user.<br>
    Use case ends.

**Extensions**
* 2a. User sees that the list is empty.
  * 2a1. User stops as there is no client to edit.
  Use case ends.
* 3a. ClientCare detects that the given command is invalid.
  * 3a1. ClientCare shows an error message.
  * 3a2. ClientCare requests for the correct input.
  * 3a3. User enters new data.<br>
    Steps 3a1-3a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 4.
* 3b. ClientCare detects that the client does not exist.
  * 3b1. ClientCare shows an error message.<br>
    Use case ends.


**Use case: UC04 - List all clients**

**MSS**
1.  User requests to view all clients.
2.  ClientCare shows a list of all clients.
3.  ClientCare shows a success message to the user.<br>
    Use case ends.

**Extensions**
* 1a. ClientCare detects that the given command is invalid.
  * 1a1. ClientCare shows an error message.
  * 1a2. ClientCare requests for the correct input.
  * 1a3. User enters new data.<br>
    Steps 1a1-1a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 2.

**Use case: UC05 - View client details and policies**

**MSS**
1.  User requests to view a client's details and policies.
2.  ClientCare shows that client's details and policies.
3.  ClientCare shows a success message to the user.<br>
    Use case ends.

**Extensions**
* 1a. ClientCare detects that the given command is invalid.
  * 1a1. ClientCare shows an error message.
  * 1a2. ClientCare requests for the correct input.
  * 1a3. User enters new data.<br>
    Steps 1a1-1a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 2.
* 1b. ClientCare detects that the client does not exist.
  * 1b1. ClientCare shows an error message.<br>
    Use case ends.



**Use case: UC06 - Find a client by name**

**MSS**
1.  User requests to find a specific client in the list by name.
2.  ClientCare shows list of client that matches the name.<br>
3.  ClientCare shows the client details.
4.  ClientCare shows a success message to the user.<br>
    Use case ends.

**Extensions**
* 1a. ClientCare detects that the given command is invalid.
  * 1a1. ClientCare shows an error message.
  * 1a2. ClientCare requests for the correct input.
  * 1a3. User enters new data.<br>
    Steps 1a1-1a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 2.
* 1b. ClientCare detects that there is no matching name found.
  * 1b1. ClientCare lets user know that the list is empty.<br>
    Use case ends.

**Use case: UC07 - Adding remark to client**

**MSS**
1. PLACEHOLDER

**Extensions**


**Use case: UC08 - Clear all client**

**MSS**
1.  User requests to clear all client data.
2.  ClientCare clears all client data.
3.  ClientCare shows a success message to the user.<br>
    Use case ends.

**Extensions**
* 1a. ClientCare detects that the given command is invalid.
  * 1a1. ClientCare shows an error message.
  * 1a2. ClientCare requests for the correct input.
  * 1a3. User enters new data.<br>
    Steps 1a1-1a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 2.

**Use case: UC09 - Sort clients**

**MSS**
1. PLACEHOLDER

**Extensions**


**Use case: UC10 - Update client as met**

**MSS**
1.  User marks a client as met.
2.  ClientCare updates Last Met date of client.<br>
    Use case ends.

**Extensions**
* 1a. ClientCare detects that the given command is invalid.
  * 1a1. ClientCare shows an error message.
  * 1a2. ClientCare requests for the correct input.
  * 1a3. User enters new data.<br>
    Steps 1a1-1a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 2.
* 1b. ClientCare detects that the given date is of the future.
  * 1b1. ClientCare shows an error message.<br>
    Use case ends.
* 1c. ClientCare detects that the client does not exist.
  * 1c1. ClientCare shows an error message.<br>
    Use case ends.

**Use case: UC11 - Update last met overdue duration**

**MSS**
1.  User enters the last met overdue duration to the new desired value.
2.  ClientCare updates the new last met overdue duration.
3.  ClientCare shows a success message to the user.<br>
    Use case ends.

**Extensions**
* 1a. ClientCare detects that the given command is invalid.
  * 1a1. ClientCare shows an error message.
  * 1a2. ClientCare requests for the correct input.
  * 1a3. User enters new data.<br>
    Steps 1a1-1a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 2.
* 1b. ClientCare detects that the given input entered is not a non-negative integer.
  * 1b1. ClientCare shows an error message.<br>
    Use case ends.
      
**Use case: UC12 - Schedule an appointment with client**

**MSS**
1.  User schedules a date and time to meet with a client.
2.  ClientCare adds the appointment to the schedule list.
3.  ClientCare shows a success message to the user.<br>
    Use case ends.

**Extensions**
* 1a. ClientCare detects that the given command is invalid.
  * 1a1. ClientCare shows an error message.
  * 1a2. ClientCare requests for the correct input.
  * 1a3. User enters new data.<br>
    Steps 1a1-1a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 2.
* 1b. ClientCare detects that the given Date and time has past.
  * 1b1. ClientCare shows error message.<br>
    Use case ends.
* 1c. ClientCare detects that the client does not exist.
  * 1c1. ClientCare shows an error message.<br>
    Use case ends.


**Use case: UC13 - Mark appointment**

**MSS**
1.  User marks an appointment with client as done.
2.  ClientCare marks the appointment as done and removes it from the schedule list.
3.  ClientCare shows a success message to the user.<br>
    Use case ends.

**Extensions**
* 1a. ClientCare detects that the given command is invalid.
  * 1a1. ClientCare shows an error message.
  * 1a2. ClientCare requests for the correct input.
  * 1a3. User enters new data.<br>
    Steps 1a1-1a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 2.
* 1b. ClientCare detects that the appointment does not exist or is already marked.
  * 1b1. ClientCare shows an error message.<br>
    Use case ends.
* 1c. ClientCare detects that the client does not exist.
  * 1c1. ClientCare shows an error message.<br>
    Use case ends.

**Use case: UC14 - Add policies to client**

**MSS**
1.  User requests to add a policy to a client.
2.  ClientCare adds the policy to the client.
3.  ClientCare shows a success message to the user.<br>
    Use case ends.

**Extensions**
* 1a. ClientCare detects that the given command is invalid.
  * 1a1. ClientCare shows an error message.
  * 1a2. ClientCare requests for the correct input.
  * 1a3. User enters new data.<br>
    Steps 1a1-1a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 2.
* 1b. ClientCare detects that the client already has a policy with the given policy id.
  * 1b1. ClientCare shows an error message.<br>
    Use case ends.
* 1c. ClientCare detects that the client does not exist.
  * 1c1. ClientCare shows an error message.<br>
    Use case ends.

**Use case: UC15 - Delete policies from client**

**MSS**
1.  User requests to delete a policy from a client.
2.  ClientCare deletes the policy from the client.
3.  ClientCare shows a success message to the user.<br>
    Use case ends.

**Extensions**
* 1a. ClientCare detects that the given command is invalid.
  * 1a1. ClientCare shows an error message.
  * 1a2. ClientCare requests for the correct input.
  * 1a3. User enters new data.<br>
    Steps 1a1-1a3 are repeated until the data entered are correct.<br>
    Use case resumes at step 2.
* 1b. ClientCare detects that the client does not have a policy with the given policy id.
  * 1b1. ClientCare shows an error message.<br>
    Use case ends.
* 1c. ClientCare detects that the client does not exist.
  * 1c1. ClientCare shows an error message.<br>
    Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The system should respond to user input within 2 seconds.
5.  The user interface should be intuitive and easy to use, even for users with limited technical knowledge. This includes providing clear and concise instructions, organizing information logically, and offering helpful error messages and tooltips.
6.  The codebase should be well-structured, modular, and documented to facilitate future maintenance and enhancements. This includes adhering to coding standards, using version control, and providing comprehensive developer documentation.

*{More to be added}*

### Glossary

* **Client**: Customers or potential customers the insurance agent wants to keep in contact with
* **Command Line Interface (CLI)**: A text-based interface to input commands to interact with the system
* **Graphical User Interface (GUI)**: A visual interface to interact with the system
* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **AddressBook**: The underlying system that ClientCare is built on. Interchangeable with ClientCare
* **Client Priority**: The level of importance or significance assigned to a client, which may influence the order of interactions or services provided
* **Policy**: An agreement or contract between an insurance company and a client, specifying the terms and conditions of insurance coverage
* **Scheduler**: A feature of the ClientCare application that allows users to manage and organize appointments and follow-ups with clients
* **Reminder**: A notification or alert generated by the ClientCare application to remind users of upcoming appointments or follow-ups with clients
* **Last Met**: The date on which the user last interacted with a client, used for tracking and monitoring client interactions
* **Refresh**: A command or action that updates the information displayed in the ClientCare application to reflect the most recent data
* **Help**: A feature of the ClientCare application that provides assistance, guidance, or instructions to users on how to use the application

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually. 
The full guide on the expected outcome and format of the commands can be found in our user guide.
This appendix will serve to inform users on the assumptions along with a correct and invalid test case.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. _{ more test cases …​ }_

### Adding a client

### Deleting a client

1. Deleting a client while all clients are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   2. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   3. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   4. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

2. _{ more test cases …​ }_

### Editing a client

### Listing client

1. Listing all clients

   1. Prerequisites: Multiple clients in the client list.

   2. Test case: `list`<br>
      Expected: All clients are shown in the list view. Success message shown in the status message.

   3. Test case: `list 1`, `list asdsad`, `list n/Jones` or any command with extra characters supplied<br>
      Expected: Similar to previous.


### Viewing a client

1. Viewing a client while all clients are being shown

   1. Prerequisites: List all clients using the `list` command. Multiple clients in the client list.

   2. Test case: `view 1`<br>
      Expected: First client's details are shown in the client details view and policy details view. Success message shown in the status message.

   3. Test case (Missing Index): `view`<br>
      Expected: Client details view and Policy details view not updated. Error message shown in the status message.

   4. Test case (Invalid Index): `view x` (where x is smaller or larger than the list size)<br>
      Expected: Similar to previous.

   6. Test case (Extra Characters): `view 1 asd`, `view 1 n/Jones` or any command with extra characters supplied<br>
      Expected: Similar to previous.

### Finding a client

1. Finding a client with a given name `Jones`

   1. Prerequisites: List all client using the `list` command. Multiple clients in the client list. Ensure there is a client with the name "Jones".

   2. Test case: `find Jones`<br>
      Expected: Client list update to show all clients with the name "Jones". Success message shown in the status message.

   3. Test case (Multiple keywords): `find Jones Brown`<br>
      Expected: Client list update to show all clients with the name "Jones" or the name "Brown" (if any). Success message shown in the status message.

   4. Test case (Missing keyword): `find`<br>
      Expected: No client is found. Error message shown in the status message.
   
### Adding notes to a client

### Clearing the client list

1. Clearing the client list

   1. Prerequisites: Multiple clients in the client list.

   2. Test case: `clear`<br>
      Expected: All clients are removed from the list. Success message shown in the status message.

   3. Test case: `clear 1`, `clear asdsad`, `clear n/Jones` or any command with extra characters supplied<br>
      Expected: Similar to previous.

### Sorting clients

### Updating last met

1. Updating the last met of a client while all clients are being shown
   1. Prerequisites: List all clients using the `list` command. Multiple clients in the client list. Ensure the date chosen is not in the future.
   For the test cases, we assume that today is 13 April 2024.

   2. Test case: `met 1 d/2024-04-11`<br>
      Expected: Last met successfully updated for first client. Success message shown in the status message.
   
   3. Test case (Invalid Date Format): `met 3 d/11-04-2024`<br>
      Expected: Last met not updated for any client. Error details in status message.
   
   4. Test case (Missing Parameters): `met d/2024-04-11`, `met 3` or any command with missing parameters<br>
      Expected: Similar to previous.
   
   5. Test case (Repeated Parameters): `met 3 d/2024-04-11 d/2024-04-11` or any command with repeated parameter<br>
      Expected: Similar to previous.
   
   6. Test case (Invalid Index): `met x d/2024-04-11` (where x is smaller or larger than the list size)<br>
      Expected: Similar to previous.
   
   7. Test Case (Future Date, Invalid Date): `met 3 d/2025-04-20`
      Expected: Similar to previous.
   
   8. Test Case (Invalid Date): `schedule 3 d/2024-02-31`
      Expected: Similar to previous.
   
### Scheduling an appointment

1. Scheduling an appointment with a client while all clients are being shown
   1. Prerequisites: List all clients using the `list` command. Multiple clients in the client list. Ensure the date chosen is in the future.
     For the test cases, we assume that today is 13 April 2024 and the time is 2pm.

   2. Test case: `schedule 1 d/2025-04-18 18:00`<br>
      Expected: Schedule successfully updated for first client. Success message shown in the status message.

   3. Test case (Invalid DateTime Format): `schedule 3 d/18-04-2025`<br>
      Expected: Schedule not updated for any client. Error details in status message.

   4. Test case (Missing Parameters): `schedule d/2025-04-18 18:00`, `schedule 3` or any command with missing parameters<br>
      Expected: Similar to previous.

   5. Test case (Repeated Parameters): `schedule 3 d/2025-04-18 18:00 d/2025-05-17 13:15` or any command with repeated parameter<br>
      Expected: Similar to previous.

   6. Test case (Invalid Index): `schedule x d/2025-04-18 13:00` (where x is smaller or larger than the list size)<br>
      Expected: Similar to previous.

   7. Test Case (Non-Future DateTime, Invalid DateTime): `schedule 3 d/2024-04-10 12:00`, `schedule 3 d/2024-04-13 14:00`
      Expected: Similar to previous.
   
   8. Test Case (Invalid DateTime): `schedule 3 d/2025-02-31 12:00`
      Expected: Similar to previous.

### Marking an appointment as complete
1. Updating an appointment with a client as completed while all clients are being shown
   1. Prerequisites: List all clients using the `list` command. Multiple clients in the client list. Ensure there is an open appointment with an existing client.

   2. Test case: `mark 1`<br>
      Expected: Appointment with the first client is successfully updated as completed. Success message shown in the status message.

   3. Test case (No Open Appointment with existing client): `mark 2`<br>
      Expected: Appointment not updated for any client. Error details in status message.

   4. Test case (Missing Parameters): `mark`<br>
      Expected: Similar to previous.

   5. Test case (Repeated Parameters): `schedule 3 3`<br>
      Expected: Similar to previous.

   6. Test case (Invalid Index): `mark x` (where x is smaller or larger than the list size)<br>
      Expected: Similar to previous.

### Set the last met overdue duration
1. Setting a new last met overdue duration
   1. Prerequisites: Nil

   2. Test case: `set 45`<br>
      Expected: Sets the new last met overdue duration to 45 days. Success message shown in the status message.
   
   3. Test case (Missing Parameters): `set`<br>
      Expected: Last met overdue duration remains unchanged. Error details in status message.
   
   4. Test Case (Non-numerical, Invalid Parameter): `set abc`<br>
      Expected: Similar to previous.
   
   5. Test Case (Non-Integer, Invalid Parameter): `set 64.6`<br>
      Expected: Similar to previous.
   
   6. Test Case (Negative Integer, Invalid Parameter): `set -6`<br>
      Expected: Similar to previous.
   
   7. Test Case (Value Above Integer Limit): `set 1234567890098765432112345564354345324343124134211232132131231`
      Expected: Similar to previous.
   
### Adding a policy

1. Adding a policy to a patient while all clients are being shown

   1. Prerequisites: List all clients using the `list` command. Multiple clients in the client list. Ensure the first client does not have the policy number "123".<br>
   
   2. Test case: `addpolicy 1 n/Health i/123`<br>
      Expected: Policy successfully added to first client. Success message shown in the status message.

   3. Test case (Missing Index): `addpolicy n/Health i/123`<br>
      Expected: Policy not added to any client. Error message shown in the status message.

   4. Test case (Missing Parameters): `addpolicy 1 n/Health`, `addpolicy 1 i/123` or any command with missing parameters<br>
      Expected: Similar to previous.
   
   5. Test case (Invalid Index): `addpolicy x n/Health i/123` (where x is smaller or larger than the list size)<br>
      Expected: Similar to previous.
   
   6. Test case (Invalid Policy Name): `addpolicy 1 n/#Health i/123`<br>
      Expected: Similar to previous.

   7. Test case (Invalid Policy Number): `addpolicy 1 n/Health i/abc`<br>
      Expected: Similar to previous.
   
   8. Test case (Repeated Parameters): `addpolicy 1 n/Health i/123 n/Health` or any command with repeated parameter<br>
      Expected: Similar to previous.

### Deleting a policy

1. Deleting a policy from a patient while all clients are being shown

   1. Prerequisites: List all clients using the `list` command. Multiple clients in the client list. Ensure the first client has the policy number "123".<br>

   2. Test case: `deletepolicy 1 i/123`<br>
   Expected: Policy successfully added to first client. Success message shown in the status message.

   3. Test case (Missing Index): `deletepolicy i/123`<br>
     Expected: Policy not added to any client. Error message shown in the status message.

   4. Test case (Missing Parameters): `deletepolicy 1 `<br>
     Expected: Similar to previous.

   5. Test case (Invalid Index): `deletepolicy x i/123` (where x is smaller or larger than the list size)<br>
     Expected: Similar to previous.

   6. Test case (Invalid Policy Number): `deletepolicy 1 i/abc`<br>
       Expected: Similar to previous.

   7. Test case (Repeated Parameters): `deletepolicy 1 i/123 i/123`<br>
       Expected: Similar to previous.

### Saving data

1. Dealing with missing/corrupted clientcare.json

   1. Prerequisites: The clientcare.json file exists in the data directory.

   2. Test case: Delete the clientcare.json file.<br>
   Expected: The app launches successfully, populated with the sample data.

   3. Test case: Delete contents of clientcare.json file.<br>
   Expected: The app launches successfully, populated with no data.

   4. Test case: Add random characters to json file that affects the formatting of the file.<br>
    Expected: Similar to previous.

2. Dealing with wrongly edit clientcare.json

   1. Prerequisites: The clientcare.json file exists in the data directory.

   2. Test case: Remove fields from clients.<br>
   Expected: The app launches successfully, populated with no data.

   3. Test case: Reorder of fields in client.<br>
   Expected: Similar to previous.

3. Dealing with missing/corrupted setvalue.txt

   1. Prerequisites: The setvalue.txt file exists in the data directory.

   2. Test case: Delete the setvalue.txt file.<br>
   Expected: The app launches successfully, with the default overdue period value.

   3. Test case: Empty setvalue.txt file.<br>
   Expected: Similar to previous.

   4. Test case: Add non-digit characters to setvalue.txt file.<br>
   Expected: Similar to previous.
   
   5. Test case: Add non-integer values to setvalue.txt file.<br>
   Expected: Similar to previous.
   
   6. Test Case: Add negative integer values to setvalue.txt file.<br>
   Expected: Similar to previous.

4. Dealing with wrongly edit setvalue.txt
   
    1. Prerequisites: The setvalue.txt file exists in the data directory.
    
    2. Test case: Edit value to be over integer limit.<br>
    Expected: The app launches successfully, with the default overdue period value.
    
    3. Test case: Add non-digit characters to the file.<br>
    Expected: Similar to previous.
    
    4. Test case: Edit value to be negative.<br>
    Expected: Similar to previous.
   
    5. Test case: Edit value to be non-integer.<br>
    Expected: Similar to previous.

## **Appendix: Planned Enhancements**

Team Size: 4

1. **Feature Flaw** - Currently, users can only schedule one appointment per client. In future versions, we will support multiple appointments per client.
2. **Feature Flaw** - Currently, names are case-sensitive. `John` and `john` are regarded as different clients. In future versions, names will be case-insensitive.
3. **Feature Flaw** - Currently, tags only allow alphanumeric values. Spaces and special characters are not allowed. In future versions, we will support the use of spaces and special characters for tags.
4. **Feature Flaw** - Currently, users are not allowed to use special characters like `/` when adding or editing the client name. In future versions, we will support the use of special characters like `/` for names.
5. **Feature Flaw** - Currently, users must fulfill all compulsory parameters to add a client. In future versions, we will make more parameters optional.
6. **Feature Flaw** - Currently, users must re-sort the client list after adding, editing or updating clients. Client List does not auto update or re-sort itself upon adding/editing or updating. In future versions, we will support the auto sorting when clients are updated.
7. **UI Bug** - Currently, the policy name and policy id may get truncated if there are too many characters. In future versions, we will support the wrapping of fields in the Policy Display.
8. **UI Bug** - Currently, the phone number and remark may get truncated if they are too long. In future versions, we will support the wrapping of all fields in the Client View Display.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Effort**

1. Scheduling Features
`met`, `schedule`, `mark` and `set` commands help the user manage his scheduling matters. As these 4 commands directly affect each other, the difficulty comes in thinking what and how
their respective class methods should interact with each other, especially with what date format to choose as this directly affects our auto-sort implemented for scheduling. It is also difficult to test for extreme cases that may cause these commands to misbehave.
We decided to simplify the process by restricting the user to 1 appointment per client as our initial beta version faced multiple bugs due to higher number of classes and functions when supporting multiple appointments per client.

`set` is also saved in a separate txt file as it is not related to client traits. Hence, additional testing is needed to ensure the value that `set` updates is saved correctly and is able to handle
invalid values if the txt file is edited wrongly.

2. Policies Features

3. Additional Client Traits and Features

4. GUI

--------------------------------------------------------------------------------------------------------------------
