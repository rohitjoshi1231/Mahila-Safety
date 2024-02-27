package com.domingo.mahila_saftey

fun data(): String {
    val globalStatistics = mutableListOf(
        "Globally, about 1 in 3 women have experienced physical and/or sexual violence by an intimate partner or sexual violence by a non-partner in their lifetime (WHO).",
        "Every 73 seconds, an American is sexually assaulted (RAINN).",
        "Approximately 1 in 3 women worldwide have experienced physical and/or sexual intimate partner violence or non-partner sexual violence in their lifetime (WHO).",
        "Nearly 20 people per minute are physically abused by an intimate partner in the United States (NCRB).",
        "Women and girls make up 71% of human trafficking victims worldwide, with the primary purpose being sexual exploitation (UN).",
        "Thousands of women and girls are killed each year worldwide in the name of \"honor\" (Estimates).",
        "Over 200 million girls and women alive today have undergone FGM in the 30 countries with representative data on its prevalence (UNICEF).",
        "In conflict-affected settings, rates of violence against women and girls can be up to 20 times higher than in peaceful settings, according to the UN.",
        "About 137 women across the world are killed by a member of their own family every day, according to UN data.",
        "Globally, only 40% of women who experience violence seek help, and less than 10% of those seek help from the police, according to UN Women.",
        "Approximately 15 million adolescent girls worldwide have experienced forced sex at some point in their lives (UNFPA).",
        "Globally, around 650 million women alive today were married before their 18th birthday (UNICEF).",
        "Women in the age group of 15-44 are more at risk from rape and domestic violence than from cancer, motor accidents, war, and malaria, according to the World Bank.",
        "Around 200 million women and girls alive today have undergone female genital mutilation in 30 countries with representative data (UNICEF).",
        "An estimated 25% of women in the European Union have experienced physical and/or sexual violence since the age of 15 (European Institute for Gender Equality).",
        "In Australia, one in six women and one in 16 men have experienced physical or sexual violence from a current or former partner (Australian Bureau of Statistics).",
        "About 60% of women in the Middle East and North Africa have experienced some form of gender-based violence in public spaces, according to a UN report.",
        "In Japan, the reported rate of sexual violence is lower compared to many countries, but there is significant underreporting due to social stigma and fear of retaliation.",
        "In Latin America, an estimated 3,529 women were victims of femicide in 2018, highlighting the alarming rates of gender-based violence in the region (ECLAC).",
        "In sub-Saharan Africa, gender-based violence is a widespread issue, with cultural norms, poverty, and conflict contributing to high rates of abuse (World Bank)."
    )

    val indiaSpecificStatistics = mutableListOf(
        "In 2019, there were 32,033 reported cases of rape in India (NCRB).",
        "About 30% of women aged 15-9 in India have experienced physical violence since the age of 15, and around 6% have experienced sexual violence (NFHS).",
        "In 2019, there were 7,026 reported cases of dowry deaths in India (NCRB).",
        "A survey found that 95% of women in Delhi felt unsafe in public spaces (Breakthrough).",
        "India serves as a source, destination, and transit country for human trafficking, with thousands of women and children trafficked within the country (U.S. State Department).",
        "The 2011 Census showed highly skewed child sex ratios in several states in India, indicating the prevalence of female infanticide and gender raised discrimination.",
        "In India, around 27% of adolescent girls have experienced physical violence, and about 25% have experienced emotional violence, according to NFHS.",
        "India's conviction rate for crimes against women is only about 27%, according to NCRB data.",
        "Despite legal bans, child marriage still persists in many parts of India, with around 27% of girls married before the age of 18, according to UNICEF.",
        "In India, nearly 90% of human trafficking victims are women and girls, with many forced into sexual exploitation (UNODC).",
        "India has the largest number of child brides in the world, with more than 15 million girls married before the age of 18 (Girls Not Brides).",
        "Approximately 56% of Indian women have experienced some form of violence by their intimate partner, according to the National Family Health Survey (NFHS-5).",
        "Only about 14% of Indian women who experience violence seek help, and of those who seek help, only 7% approach the police (NFHS-5).",
        "India accounts for 18% of the global burden of mental illness among women, with many facing stigma and limited access to mental health services (Lancet Psychiatry).",
        "In rural areas of India, women's access to education and healthcare is often limited, exacerbating gender disparities (World Bank).",
        "According to the National Crime Records Bureau, acid attacks against women in India have been on the rise, with over 300 reported cases in 2019.",
        "Despite advancements, there is still a gender pay gap in India, with women earning significantly less than their male counterparts in various sectors (World Economic Forum).",
        "India has made strides in promoting women's political participation, but there is still a need for increased representation at higher levels of governance (UN Women).",
        "Access to sanitation remains a challenge for many women in India, impacting their health, dignity, and overall well-being (WaterAid India).",
        "India has implemented various programs and initiatives to empower women economically, but there is ongoing work to address barriers to entrepreneurship and employment (NITI Aayog)."
    )

    val allData = mutableListOf<String>()

    allData.addAll(globalStatistics)
    allData.addAll(indiaSpecificStatistics)

    allData.shuffle()

    return allData.first()

}