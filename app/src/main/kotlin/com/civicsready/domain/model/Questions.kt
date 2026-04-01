package com.civicsready.domain.model

val ALL_QUESTIONS: List<CivicsQuestion> = listOf(

    // ── AMERICAN GOVERNMENT: A – Principles of American Government ────────────

    CivicsQuestion(
        id = 1,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "What is the form of government of the United States?",
        acceptableAnswers = listOf("Republic", "Constitution-based federal republic", "Representative democracy")
    ),
    CivicsQuestion(
        id = 2,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "What is the supreme law of the land?",
        acceptableAnswers = listOf("(U.S.) Constitution", "The Constitution"),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 3,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "Name one thing the U.S. Constitution does.",
        acceptableAnswers = listOf(
            "Forms the government",
            "Defines powers of government",
            "Defines the parts of government",
            "Protects the rights of the people"
        )
    ),
    CivicsQuestion(
        id = 4,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "The U.S. Constitution starts with the words \"We the People.\" What does \"We the People\" mean?",
        acceptableAnswers = listOf(
            "Self-government",
            "Popular sovereignty",
            "Consent of the governed",
            "People should govern themselves",
            "Example of social contract"
        )
    ),
    CivicsQuestion(
        id = 5,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "How are changes made to the U.S. Constitution?",
        acceptableAnswers = listOf("Amendments", "The amendment process")
    ),
    CivicsQuestion(
        id = 6,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "What does the Bill of Rights protect?",
        acceptableAnswers = listOf(
            "The basic rights of Americans",
            "The basic rights of people living in the United States"
        )
    ),
    CivicsQuestion(
        id = 7,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "How many amendments does the U.S. Constitution have?",
        acceptableAnswers = listOf("Twenty-seven (27)", "27"),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 8,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "Why is the Declaration of Independence important?",
        acceptableAnswers = listOf(
            "It says America is free from British control.",
            "It says all people are created equal.",
            "It identifies inherent rights.",
            "It identifies individual freedoms."
        )
    ),
    CivicsQuestion(
        id = 9,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "What founding document said the American colonies were free from Britain?",
        acceptableAnswers = listOf("Declaration of Independence")
    ),
    CivicsQuestion(
        id = 10,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "Name two important ideas from the Declaration of Independence and the U.S. Constitution.",
        acceptableAnswers = listOf(
            "Equality", "Liberty", "Social contract", "Natural rights", "Limited government", "Self-government"
        ),
        minimumAnswersRequired = 2
    ),
    CivicsQuestion(
        id = 11,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "The words \"Life, Liberty, and the pursuit of Happiness\" are in what founding document?",
        acceptableAnswers = listOf("Declaration of Independence")
    ),
    CivicsQuestion(
        id = 12,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "What is the economic system of the United States?",
        acceptableAnswers = listOf("Capitalism", "Free market economy"),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 13,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "What is the rule of law?",
        acceptableAnswers = listOf(
            "Everyone must follow the law.",
            "Leaders must obey the law.",
            "Government must obey the law.",
            "No one is above the law."
        )
    ),
    CivicsQuestion(
        id = 14,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "Many documents influenced the U.S. Constitution. Name one.",
        acceptableAnswers = listOf(
            "Declaration of Independence",
            "Articles of Confederation",
            "Federalist Papers",
            "Anti-Federalist Papers",
            "Virginia Declaration of Rights",
            "Fundamental Orders of Connecticut",
            "Mayflower Compact",
            "Iroquois Great Law of Peace"
        )
    ),
    CivicsQuestion(
        id = 15,
        section = Section.PRINCIPLES_OF_GOVERNMENT,
        text = "There are three branches of government. Why?",
        acceptableAnswers = listOf(
            "So one part does not become too powerful",
            "Checks and balances",
            "Separation of powers"
        )
    ),

    // ── AMERICAN GOVERNMENT: B – System of Government ─────────────────────────

    CivicsQuestion(
        id = 16,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Name the three branches of government.",
        acceptableAnswers = listOf(
            "Legislative, executive, and judicial",
            "Congress, president, and the courts"
        )
    ),
    CivicsQuestion(
        id = 17,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "The President of the United States is in charge of which branch of government?",
        acceptableAnswers = listOf("Executive branch")
    ),
    CivicsQuestion(
        id = 18,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "What part of the federal government writes laws?",
        acceptableAnswers = listOf(
            "(U.S.) Congress", "U.S. Congress", "U.S. or national legislature", "Legislative branch"
        )
    ),
    CivicsQuestion(
        id = 19,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "What are the two parts of the U.S. Congress?",
        acceptableAnswers = listOf("Senate and House of Representatives", "Senate and House")
    ),
    CivicsQuestion(
        id = 20,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Name one power of the U.S. Congress.",
        acceptableAnswers = listOf("Writes laws", "Declares war", "Makes the federal budget"),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 21,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "How many U.S. senators are there?",
        acceptableAnswers = listOf("One hundred (100)", "100")
    ),
    CivicsQuestion(
        id = 22,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "How long is a term for a U.S. senator?",
        acceptableAnswers = listOf("Six (6) years", "6 years")
    ),
    CivicsQuestion(
        id = 23,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Who is one of your state's U.S. senators now?",
        acceptableAnswers = listOf("Answers will vary"),
        dynamicAnswerType = DynamicAnswerType.SENATOR
    ),
    CivicsQuestion(
        id = 24,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "How many voting members are in the House of Representatives?",
        acceptableAnswers = listOf("Four hundred thirty-five (435)", "435")
    ),
    CivicsQuestion(
        id = 25,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "How long is a term for a member of the House of Representatives?",
        acceptableAnswers = listOf("Two (2) years", "2 years")
    ),
    CivicsQuestion(
        id = 26,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Why do U.S. representatives serve shorter terms than U.S. senators?",
        acceptableAnswers = listOf("To more closely follow public opinion")
    ),
    CivicsQuestion(
        id = 27,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "How many senators does each state have?",
        acceptableAnswers = listOf("Two (2)", "2")
    ),
    CivicsQuestion(
        id = 28,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Why does each state have two senators?",
        acceptableAnswers = listOf(
            "Equal representation (for small states)",
            "The Great Compromise (Connecticut Compromise)"
        )
    ),
    CivicsQuestion(
        id = 29,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Name your U.S. representative.",
        acceptableAnswers = listOf("Answers will vary"),
        dynamicAnswerType = DynamicAnswerType.REPRESENTATIVE
    ),
    CivicsQuestion(
        id = 30,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "What is the name of the Speaker of the House of Representatives now?",
        acceptableAnswers = listOf("Mike Johnson"),
        dynamicAnswerType = DynamicAnswerType.SPEAKER_OF_HOUSE,
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 31,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Who does a U.S. senator represent?",
        acceptableAnswers = listOf("Citizens of their state", "People of their state")
    ),
    CivicsQuestion(
        id = 32,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Who elects U.S. senators?",
        acceptableAnswers = listOf("Citizens from their state")
    ),
    CivicsQuestion(
        id = 33,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Who does a member of the House of Representatives represent?",
        acceptableAnswers = listOf(
            "Citizens in their congressional district",
            "Citizens in their district",
            "People from their congressional district",
            "People in their district"
        )
    ),
    CivicsQuestion(
        id = 34,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Who elects members of the House of Representatives?",
        acceptableAnswers = listOf("Citizens from their congressional district")
    ),
    CivicsQuestion(
        id = 35,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Some states have more representatives than other states. Why?",
        acceptableAnswers = listOf(
            "Because of the state's population",
            "Because they have more people",
            "Because some states have more people"
        )
    ),
    CivicsQuestion(
        id = 36,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "The President of the United States is elected for how many years?",
        acceptableAnswers = listOf("Four (4) years", "4 years"),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 37,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "The President of the United States can serve only two terms. Why?",
        acceptableAnswers = listOf(
            "Because of the 22nd Amendment",
            "To keep the president from becoming too powerful"
        )
    ),
    CivicsQuestion(
        id = 38,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "What is the name of the President of the United States now?",
        acceptableAnswers = listOf("Donald Trump"),
        dynamicAnswerType = DynamicAnswerType.PRESIDENT,
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 39,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "What is the name of the Vice President of the United States now?",
        acceptableAnswers = listOf("JD Vance", "J.D. Vance"),
        dynamicAnswerType = DynamicAnswerType.VICE_PRESIDENT,
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 40,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "If the president can no longer serve, who becomes president?",
        acceptableAnswers = listOf("The Vice President of the United States", "The Vice President")
    ),
    CivicsQuestion(
        id = 41,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Name one power of the president.",
        acceptableAnswers = listOf(
            "Signs bills into law",
            "Vetoes bills",
            "Enforces laws",
            "Commander in Chief of the military",
            "Chief diplomat",
            "Appoints federal judges"
        )
    ),
    CivicsQuestion(
        id = 42,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Who is Commander in Chief of the U.S. military?",
        acceptableAnswers = listOf("The President of the United States", "The President")
    ),
    CivicsQuestion(
        id = 43,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Who signs bills to become laws?",
        acceptableAnswers = listOf("The President of the United States", "The President")
    ),
    CivicsQuestion(
        id = 44,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Who vetoes bills?",
        acceptableAnswers = listOf("The President of the United States", "The President"),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 45,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Who appoints federal judges?",
        acceptableAnswers = listOf("The President of the United States", "The President")
    ),
    CivicsQuestion(
        id = 46,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "The executive branch has many parts. Name one.",
        acceptableAnswers = listOf(
            "President of the United States",
            "Cabinet",
            "Federal departments and agencies"
        )
    ),
    CivicsQuestion(
        id = 47,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "What does the President's Cabinet do?",
        acceptableAnswers = listOf("Advises the President of the United States", "Advises the President")
    ),
    CivicsQuestion(
        id = 48,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "What are two Cabinet-level positions?",
        acceptableAnswers = listOf(
            "Attorney General",
            "Secretary of Agriculture",
            "Secretary of Commerce",
            "Secretary of Education",
            "Secretary of Energy",
            "Secretary of Health and Human Services",
            "Secretary of Homeland Security",
            "Secretary of Housing and Urban Development",
            "Secretary of the Interior",
            "Secretary of Labor",
            "Secretary of State",
            "Secretary of Transportation",
            "Secretary of the Treasury",
            "Secretary of Veterans Affairs",
            "Secretary of Defense",
            "Vice President",
            "Administrator of the Environmental Protection Agency",
            "Administrator of the Small Business Administration",
            "Director of the Central Intelligence Agency",
            "Director of the Office of Management and Budget",
            "Director of National Intelligence",
            "United States Trade Representative"
        ),
        minimumAnswersRequired = 2
    ),
    CivicsQuestion(
        id = 49,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Why is the Electoral College important?",
        acceptableAnswers = listOf(
            "It decides who is elected president.",
            "It provides a compromise between the popular election of the president and congressional selection."
        )
    ),
    CivicsQuestion(
        id = 50,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "What is one part of the judicial branch?",
        acceptableAnswers = listOf("Supreme Court", "Federal Courts")
    ),
    CivicsQuestion(
        id = 51,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "What does the judicial branch do?",
        acceptableAnswers = listOf(
            "Reviews laws",
            "Explains laws",
            "Resolves disputes about the law",
            "Decides if a law goes against the U.S. Constitution"
        )
    ),
    CivicsQuestion(
        id = 52,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "What is the highest court in the United States?",
        acceptableAnswers = listOf("Supreme Court"),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 53,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "How many seats are on the Supreme Court?",
        acceptableAnswers = listOf("Nine (9)", "9")
    ),
    CivicsQuestion(
        id = 54,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "How many Supreme Court justices are usually needed to decide a case?",
        acceptableAnswers = listOf("Five (5)", "5")
    ),
    CivicsQuestion(
        id = 55,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "How long do Supreme Court justices serve?",
        acceptableAnswers = listOf("For life", "Lifetime appointment", "Until retirement")
    ),
    CivicsQuestion(
        id = 56,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Supreme Court justices serve for life. Why?",
        acceptableAnswers = listOf(
            "To be independent of politics",
            "To limit outside political influence"
        )
    ),
    CivicsQuestion(
        id = 57,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Who is the Chief Justice of the United States now?",
        acceptableAnswers = listOf("John Roberts"),
        dynamicAnswerType = DynamicAnswerType.CHIEF_JUSTICE
    ),
    CivicsQuestion(
        id = 58,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Name one power that is only for the federal government.",
        acceptableAnswers = listOf(
            "Print paper money",
            "Mint coins",
            "Declare war",
            "Create an army",
            "Make treaties",
            "Set foreign policy"
        )
    ),
    CivicsQuestion(
        id = 59,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Name one power that is only for the states.",
        acceptableAnswers = listOf(
            "Provide schooling and education",
            "Provide protection (police)",
            "Provide safety (fire departments)",
            "Give a driver's license",
            "Approve zoning and land use"
        )
    ),
    CivicsQuestion(
        id = 60,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "What is the purpose of the 10th Amendment?",
        acceptableAnswers = listOf(
            "Powers not given to the federal government belong to the states or to the people."
        )
    ),
    CivicsQuestion(
        id = 61,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "Who is the governor of your state now?",
        acceptableAnswers = listOf("Answers will vary"),
        dynamicAnswerType = DynamicAnswerType.GOVERNOR,
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 62,
        section = Section.SYSTEM_OF_GOVERNMENT,
        text = "What is the capital of your state?",
        acceptableAnswers = listOf("Answers will vary"),
        dynamicAnswerType = DynamicAnswerType.STATE_CAPITAL
    ),

    // ── AMERICAN GOVERNMENT: C – Rights and Responsibilities ─────────────────

    CivicsQuestion(
        id = 63,
        section = Section.RIGHTS_AND_RESPONSIBILITIES,
        text = "There are four amendments to the U.S. Constitution about who can vote. Describe one of them.",
        acceptableAnswers = listOf(
            "Citizens eighteen (18) and older can vote.",
            "You don't have to pay a poll tax to vote.",
            "Any citizen can vote. (Women and men can vote.)",
            "A male citizen of any race can vote."
        )
    ),
    CivicsQuestion(
        id = 64,
        section = Section.RIGHTS_AND_RESPONSIBILITIES,
        text = "Who can vote in federal elections, run for federal office, and serve on a jury in the United States?",
        acceptableAnswers = listOf("Citizens", "Citizens of the United States", "U.S. citizens")
    ),
    CivicsQuestion(
        id = 65,
        section = Section.RIGHTS_AND_RESPONSIBILITIES,
        text = "What are three rights of everyone living in the United States?",
        acceptableAnswers = listOf(
            "Freedom of expression",
            "Freedom of speech",
            "Freedom of assembly",
            "Freedom to petition the government",
            "Freedom of religion",
            "The right to bear arms"
        ),
        minimumAnswersRequired = 3
    ),
    CivicsQuestion(
        id = 66,
        section = Section.RIGHTS_AND_RESPONSIBILITIES,
        text = "What do we show loyalty to when we say the Pledge of Allegiance?",
        acceptableAnswers = listOf("The United States", "The flag"),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 67,
        section = Section.RIGHTS_AND_RESPONSIBILITIES,
        text = "Name two promises that new citizens make in the Oath of Allegiance.",
        acceptableAnswers = listOf(
            "Give up loyalty to other countries",
            "Defend the U.S. Constitution",
            "Obey the laws of the United States",
            "Serve in the military if needed",
            "Serve the nation if needed",
            "Be loyal to the United States"
        ),
        minimumAnswersRequired = 2
    ),
    CivicsQuestion(
        id = 68,
        section = Section.RIGHTS_AND_RESPONSIBILITIES,
        text = "How can people become United States citizens?",
        acceptableAnswers = listOf(
            "Be born in the United States, under the conditions set by the 14th Amendment",
            "Naturalize",
            "Derive citizenship under conditions set by Congress"
        )
    ),
    CivicsQuestion(
        id = 69,
        section = Section.RIGHTS_AND_RESPONSIBILITIES,
        text = "What are two examples of civic participation in the United States?",
        acceptableAnswers = listOf(
            "Vote",
            "Run for office",
            "Join a political party",
            "Help with a campaign",
            "Join a civic group",
            "Join a community group",
            "Give an elected official your opinion on an issue",
            "Contact elected officials",
            "Support or oppose an issue or policy",
            "Write to a newspaper"
        ),
        minimumAnswersRequired = 2
    ),
    CivicsQuestion(
        id = 70,
        section = Section.RIGHTS_AND_RESPONSIBILITIES,
        text = "What is one way Americans can serve their country?",
        acceptableAnswers = listOf(
            "Vote",
            "Pay taxes",
            "Obey the law",
            "Serve in the military",
            "Run for office",
            "Work for local, state, or federal government"
        )
    ),
    CivicsQuestion(
        id = 71,
        section = Section.RIGHTS_AND_RESPONSIBILITIES,
        text = "Why is it important to pay federal taxes?",
        acceptableAnswers = listOf(
            "Required by law",
            "All people pay to fund the federal government",
            "Required by the U.S. Constitution (16th Amendment)",
            "Civic duty"
        )
    ),
    CivicsQuestion(
        id = 72,
        section = Section.RIGHTS_AND_RESPONSIBILITIES,
        text = "It is important for all men age 18 through 25 to register for the Selective Service. Name one reason why.",
        acceptableAnswers = listOf(
            "Required by law",
            "Civic duty",
            "Makes the draft fair, if needed"
        )
    ),

    // ── AMERICAN HISTORY: A – Colonial Period and Independence ────────────────

    CivicsQuestion(
        id = 73,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "The colonists came to America for many reasons. Name one.",
        acceptableAnswers = listOf(
            "Freedom",
            "Political liberty",
            "Religious freedom",
            "Economic opportunity",
            "Escape persecution"
        )
    ),
    CivicsQuestion(
        id = 74,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "Who lived in America before the Europeans arrived?",
        acceptableAnswers = listOf("American Indians", "Native Americans"),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 75,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "What group of people was taken and sold as slaves?",
        acceptableAnswers = listOf("Africans", "People from Africa")
    ),
    CivicsQuestion(
        id = 76,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "What war did the Americans fight to win independence from Britain?",
        acceptableAnswers = listOf(
            "American Revolution",
            "The American Revolutionary War",
            "War for American Independence"
        )
    ),
    CivicsQuestion(
        id = 77,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "Name one reason why the Americans declared independence from Britain.",
        acceptableAnswers = listOf(
            "High taxes",
            "Taxation without representation",
            "British soldiers stayed in Americans' houses (boarding, quartering)",
            "They did not have self-government",
            "Boston Massacre",
            "Boston Tea Party (Tea Act)",
            "Stamp Act",
            "Sugar Act",
            "Townshend Acts",
            "Intolerable (Coercive) Acts"
        )
    ),
    CivicsQuestion(
        id = 78,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "Who wrote the Declaration of Independence?",
        acceptableAnswers = listOf("Thomas Jefferson", "(Thomas) Jefferson"),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 79,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "When was the Declaration of Independence adopted?",
        acceptableAnswers = listOf("July 4, 1776")
    ),
    CivicsQuestion(
        id = 80,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "The American Revolution had many important events. Name one.",
        acceptableAnswers = listOf(
            "Battle of Bunker Hill",
            "Declaration of Independence",
            "Washington Crossing the Delaware (Battle of Trenton)",
            "Battle of Saratoga",
            "Valley Forge Encampment",
            "Battle of Yorktown (British surrender at Yorktown)"
        )
    ),
    CivicsQuestion(
        id = 81,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "There were 13 original states. Name five.",
        acceptableAnswers = listOf(
            "New Hampshire", "Massachusetts", "Rhode Island", "Connecticut",
            "New York", "New Jersey", "Pennsylvania", "Delaware",
            "Maryland", "Virginia", "North Carolina", "South Carolina", "Georgia"
        ),
        minimumAnswersRequired = 5
    ),
    CivicsQuestion(
        id = 82,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "What founding document was written in 1787?",
        acceptableAnswers = listOf("U.S. Constitution", "(U.S.) Constitution")
    ),
    CivicsQuestion(
        id = 83,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "The Federalist Papers supported the passage of the U.S. Constitution. Name one of the writers.",
        acceptableAnswers = listOf("James Madison", "Alexander Hamilton", "John Jay", "Publius")
    ),
    CivicsQuestion(
        id = 84,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "Why were the Federalist Papers important?",
        acceptableAnswers = listOf(
            "They helped people understand the U.S. Constitution.",
            "They supported passing the U.S. Constitution."
        )
    ),
    CivicsQuestion(
        id = 85,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "Benjamin Franklin is famous for many things. Name one.",
        acceptableAnswers = listOf(
            "Founded the first free public libraries",
            "First Postmaster General of the United States",
            "Helped write the Declaration of Independence",
            "Inventor",
            "U.S. diplomat"
        )
    ),
    CivicsQuestion(
        id = 86,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "George Washington is famous for many things. Name one.",
        acceptableAnswers = listOf(
            "\"Father of Our Country\"",
            "First president of the United States",
            "General of the Continental Army",
            "President of the Constitutional Convention"
        ),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 87,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "Thomas Jefferson is famous for many things. Name one.",
        acceptableAnswers = listOf(
            "Writer of the Declaration of Independence",
            "Third president of the United States",
            "Doubled the size of the United States (Louisiana Purchase)",
            "First Secretary of State",
            "Founded the University of Virginia",
            "Writer of the Virginia Statute on Religious Freedom"
        )
    ),
    CivicsQuestion(
        id = 88,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "James Madison is famous for many things. Name one.",
        acceptableAnswers = listOf(
            "\"Father of the Constitution\"",
            "Fourth president of the United States",
            "President during the War of 1812",
            "One of the writers of the Federalist Papers"
        )
    ),
    CivicsQuestion(
        id = 89,
        section = Section.COLONIAL_AND_INDEPENDENCE,
        text = "Alexander Hamilton is famous for many things. Name one.",
        acceptableAnswers = listOf(
            "First Secretary of the Treasury",
            "One of the writers of the Federalist Papers",
            "Helped establish the First Bank of the United States",
            "Aide to General George Washington",
            "Member of the Continental Congress"
        )
    ),

    // ── AMERICAN HISTORY: B – 1800s ──────────────────────────────────────────

    CivicsQuestion(
        id = 90,
        section = Section.EIGHTEEN_HUNDREDS,
        text = "What territory did the United States buy from France in 1803?",
        acceptableAnswers = listOf("Louisiana Territory", "Louisiana")
    ),
    CivicsQuestion(
        id = 91,
        section = Section.EIGHTEEN_HUNDREDS,
        text = "Name one war fought by the United States in the 1800s.",
        acceptableAnswers = listOf("War of 1812", "Mexican-American War", "Civil War", "Spanish-American War")
    ),
    CivicsQuestion(
        id = 92,
        section = Section.EIGHTEEN_HUNDREDS,
        text = "Name the U.S. war between the North and the South.",
        acceptableAnswers = listOf("The Civil War", "Civil War")
    ),
    CivicsQuestion(
        id = 93,
        section = Section.EIGHTEEN_HUNDREDS,
        text = "The Civil War had many important events. Name one.",
        acceptableAnswers = listOf(
            "Battle of Fort Sumter",
            "Emancipation Proclamation",
            "Battle of Vicksburg",
            "Battle of Gettysburg",
            "Sherman's March",
            "Surrender at Appomattox",
            "Battle of Antietam/Sharpsburg",
            "Lincoln was assassinated."
        )
    ),
    CivicsQuestion(
        id = 94,
        section = Section.EIGHTEEN_HUNDREDS,
        text = "Abraham Lincoln is famous for many things. Name one.",
        acceptableAnswers = listOf(
            "Freed the slaves (Emancipation Proclamation)",
            "Saved (or preserved) the Union",
            "Led the United States during the Civil War",
            "16th president of the United States",
            "Delivered the Gettysburg Address"
        ),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 95,
        section = Section.EIGHTEEN_HUNDREDS,
        text = "What did the Emancipation Proclamation do?",
        acceptableAnswers = listOf(
            "Freed the slaves",
            "Freed slaves in the Confederacy",
            "Freed slaves in the Confederate states",
            "Freed slaves in most Southern states"
        )
    ),
    CivicsQuestion(
        id = 96,
        section = Section.EIGHTEEN_HUNDREDS,
        text = "What U.S. war ended slavery?",
        acceptableAnswers = listOf("The Civil War", "Civil War")
    ),
    CivicsQuestion(
        id = 97,
        section = Section.EIGHTEEN_HUNDREDS,
        text = "What amendment says all persons born or naturalized in the United States, and subject to the jurisdiction thereof, are U.S. citizens?",
        acceptableAnswers = listOf("14th Amendment")
    ),
    CivicsQuestion(
        id = 98,
        section = Section.EIGHTEEN_HUNDREDS,
        text = "When did all men get the right to vote?",
        acceptableAnswers = listOf("After the Civil War", "During Reconstruction", "With the 15th Amendment", "1870")
    ),
    CivicsQuestion(
        id = 99,
        section = Section.EIGHTEEN_HUNDREDS,
        text = "Name one leader of the women's rights movement in the 1800s.",
        acceptableAnswers = listOf(
            "Susan B. Anthony",
            "Elizabeth Cady Stanton",
            "Sojourner Truth",
            "Harriet Tubman",
            "Lucretia Mott",
            "Lucy Stone"
        )
    ),

    // ── AMERICAN HISTORY: C – Recent American History ─────────────────────────

    CivicsQuestion(
        id = 100,
        section = Section.RECENT_HISTORY,
        text = "Name one war fought by the United States in the 1900s.",
        acceptableAnswers = listOf(
            "World War I",
            "World War II",
            "Korean War",
            "Vietnam War",
            "Persian Gulf War",
            "Gulf War"
        )
    ),
    CivicsQuestion(
        id = 101,
        section = Section.RECENT_HISTORY,
        text = "Why did the United States enter World War I?",
        acceptableAnswers = listOf(
            "Because Germany attacked U.S. civilian ships",
            "To support the Allied Powers (England, France, Italy, and Russia)",
            "To oppose the Central Powers (Germany, Austria-Hungary, the Ottoman Empire, and Bulgaria)"
        )
    ),
    CivicsQuestion(
        id = 102,
        section = Section.RECENT_HISTORY,
        text = "When did all women get the right to vote?",
        acceptableAnswers = listOf("1920", "After World War I", "With the 19th Amendment")
    ),
    CivicsQuestion(
        id = 103,
        section = Section.RECENT_HISTORY,
        text = "What was the Great Depression?",
        acceptableAnswers = listOf("Longest economic recession in modern history")
    ),
    CivicsQuestion(
        id = 104,
        section = Section.RECENT_HISTORY,
        text = "When did the Great Depression start?",
        acceptableAnswers = listOf("The Great Crash (1929)", "Stock market crash of 1929", "1929")
    ),
    CivicsQuestion(
        id = 105,
        section = Section.RECENT_HISTORY,
        text = "Who was president during the Great Depression and World War II?",
        acceptableAnswers = listOf("Franklin Roosevelt", "(Franklin) Roosevelt", "FDR")
    ),
    CivicsQuestion(
        id = 106,
        section = Section.RECENT_HISTORY,
        text = "Why did the United States enter World War II?",
        acceptableAnswers = listOf(
            "Bombing of Pearl Harbor",
            "Japanese attacked Pearl Harbor",
            "To support the Allied Powers (England, France, and Russia)",
            "To oppose the Axis Powers (Germany, Italy, and Japan)"
        )
    ),
    CivicsQuestion(
        id = 107,
        section = Section.RECENT_HISTORY,
        text = "Dwight Eisenhower is famous for many things. Name one.",
        acceptableAnswers = listOf(
            "General during World War II",
            "President at the end of the Korean War",
            "34th president of the United States",
            "Signed the Federal-Aid Highway Act of 1956 (Created the Interstate System)"
        )
    ),
    CivicsQuestion(
        id = 108,
        section = Section.RECENT_HISTORY,
        text = "Who was the United States' main rival during the Cold War?",
        acceptableAnswers = listOf("Soviet Union", "USSR", "Russia")
    ),
    CivicsQuestion(
        id = 109,
        section = Section.RECENT_HISTORY,
        text = "During the Cold War, what was one main concern of the United States?",
        acceptableAnswers = listOf("Communism", "Nuclear war")
    ),
    CivicsQuestion(
        id = 110,
        section = Section.RECENT_HISTORY,
        text = "Why did the United States enter the Korean War?",
        acceptableAnswers = listOf("To stop the spread of communism")
    ),
    CivicsQuestion(
        id = 111,
        section = Section.RECENT_HISTORY,
        text = "Why did the United States enter the Vietnam War?",
        acceptableAnswers = listOf("To stop the spread of communism")
    ),
    CivicsQuestion(
        id = 112,
        section = Section.RECENT_HISTORY,
        text = "What did the civil rights movement do?",
        acceptableAnswers = listOf("Fought to end racial discrimination")
    ),
    CivicsQuestion(
        id = 113,
        section = Section.RECENT_HISTORY,
        text = "Martin Luther King, Jr. is famous for many things. Name one.",
        acceptableAnswers = listOf(
            "Fought for civil rights",
            "Worked for equality for all Americans",
            "Worked to ensure that people would not be judged by the color of their skin, but by the content of their character"
        ),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 114,
        section = Section.RECENT_HISTORY,
        text = "Why did the United States enter the Persian Gulf War?",
        acceptableAnswers = listOf("To force the Iraqi military from Kuwait")
    ),
    CivicsQuestion(
        id = 115,
        section = Section.RECENT_HISTORY,
        text = "What major event happened on September 11, 2001 in the United States?",
        acceptableAnswers = listOf(
            "Terrorists attacked the United States",
            "Terrorists took over two planes and crashed them into the World Trade Center in New York City",
            "Terrorists took over a plane and crashed into the Pentagon in Arlington, Virginia",
            "Terrorists took over a plane originally aimed at Washington, D.C., and crashed in a field in Pennsylvania"
        ),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 116,
        section = Section.RECENT_HISTORY,
        text = "Name one U.S. military conflict after the September 11, 2001 attacks.",
        acceptableAnswers = listOf("Global War on Terror", "War on Terror", "War in Afghanistan", "War in Iraq")
    ),
    CivicsQuestion(
        id = 117,
        section = Section.RECENT_HISTORY,
        text = "Name one American Indian tribe in the United States.",
        acceptableAnswers = listOf(
            "Apache", "Blackfeet", "Cayuga", "Cherokee", "Cheyenne", "Chippewa",
            "Choctaw", "Creek", "Crow", "Hopi", "Huron", "Inupiat", "Lakota",
            "Mohawk", "Mohegan", "Navajo", "Oneida", "Onondaga", "Pueblo",
            "Seminole", "Seneca", "Shawnee", "Sioux", "Teton", "Tuscarora"
        )
    ),
    CivicsQuestion(
        id = 118,
        section = Section.RECENT_HISTORY,
        text = "Name one example of an American innovation.",
        acceptableAnswers = listOf(
            "Light bulb",
            "Automobile (cars, internal combustion engine)",
            "Skyscrapers",
            "Airplane",
            "Assembly line",
            "Landing on the moon",
            "Integrated circuit (IC)"
        )
    ),

    // ── SYMBOLS AND HOLIDAYS: A – Symbols ────────────────────────────────────

    CivicsQuestion(
        id = 119,
        section = Section.SYMBOLS,
        text = "What is the capital of the United States?",
        acceptableAnswers = listOf("Washington, D.C.")
    ),
    CivicsQuestion(
        id = 120,
        section = Section.SYMBOLS,
        text = "Where is the Statue of Liberty?",
        acceptableAnswers = listOf(
            "New York Harbor",
            "Liberty Island",
            "New Jersey",
            "Near New York City",
            "On the Hudson River"
        )
    ),
    CivicsQuestion(
        id = 121,
        section = Section.SYMBOLS,
        text = "Why does the flag have 13 stripes?",
        acceptableAnswers = listOf(
            "Because there were 13 original colonies",
            "Because the stripes represent the original colonies"
        ),
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 122,
        section = Section.SYMBOLS,
        text = "Why does the flag have 50 stars?",
        acceptableAnswers = listOf(
            "Because there is one star for each state",
            "Each star represents a state",
            "Because there are 50 states"
        )
    ),
    CivicsQuestion(
        id = 123,
        section = Section.SYMBOLS,
        text = "What is the name of the national anthem?",
        acceptableAnswers = listOf("The Star-Spangled Banner")
    ),
    CivicsQuestion(
        id = 124,
        section = Section.SYMBOLS,
        text = "The Nation's first motto was \"E Pluribus Unum.\" What does that mean?",
        acceptableAnswers = listOf("Out of many, one", "We all become one")
    ),

    // ── SYMBOLS AND HOLIDAYS: B – Holidays ───────────────────────────────────

    CivicsQuestion(
        id = 125,
        section = Section.HOLIDAYS,
        text = "What is Independence Day?",
        acceptableAnswers = listOf(
            "A holiday to celebrate U.S. independence from Britain",
            "The country's birthday"
        )
    ),
    CivicsQuestion(
        id = 126,
        section = Section.HOLIDAYS,
        text = "Name three national U.S. holidays.",
        acceptableAnswers = listOf(
            "New Year's Day",
            "Martin Luther King, Jr. Day",
            "Presidents Day (Washington's Birthday)",
            "Memorial Day",
            "Juneteenth",
            "Independence Day",
            "Labor Day",
            "Columbus Day",
            "Veterans Day",
            "Thanksgiving Day",
            "Christmas Day"
        ),
        minimumAnswersRequired = 3,
        isFor6520 = true
    ),
    CivicsQuestion(
        id = 127,
        section = Section.HOLIDAYS,
        text = "What is Memorial Day?",
        acceptableAnswers = listOf("A holiday to honor soldiers who died in military service")
    ),
    CivicsQuestion(
        id = 128,
        section = Section.HOLIDAYS,
        text = "What is Veterans Day?",
        acceptableAnswers = listOf(
            "A holiday to honor people in the U.S. military",
            "A holiday to honor people who have served in the U.S. military"
        )
    )
)

val QUESTIONS_6520 = ALL_QUESTIONS.filter { it.isFor6520 }
