package com.civicsready.data.assets

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.int
import javax.inject.Inject
import javax.inject.Singleton

data class ZipLookupResult(
    val stateCode: String,
    val stateName: String,
    val governor: String,
    val senator1: String,
    val senator2: String,
    val representative: String,
    val stateCapital: String,
    /** True when the exact zip was found; false when only the state was resolved via 3-digit prefix. */
    val isExactMatch: Boolean = true
)

@Singleton
class OfficialAssetLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    private val zipCodes: JsonObject by lazy {
        val text = context.assets.open("zip_codes.json").bufferedReader().readText()
        json.parseToJsonElement(text).jsonObject
    }

    private val officials: JsonObject by lazy {
        val text = context.assets.open("officials.json").bufferedReader().readText()
        json.parseToJsonElement(text).jsonObject
    }

    private val districts: JsonObject by lazy {
        val text = context.assets.open("districts.json").bufferedReader().readText()
        json.parseToJsonElement(text).jsonObject
    }

    /**
     * Returns officials for the given zip code.
     *
     * Resolution order:
     * 1. Exact match in zip_codes.json  → full result (state + district representative).
     * 2. 3-digit prefix lookup          → partial result (state officials only;
     *    [ZipLookupResult.isExactMatch] == false, representative is a prompt to visit house.gov).
     * 3. Neither matches               → null.
     */
    fun lookup(zip: String): ZipLookupResult? = lookupExact(zip) ?: lookupByPrefix(zip)

    // ── private helpers ──────────────────────────────────────────────────────

    private fun lookupExact(zip: String): ZipLookupResult? {
        val zipEntry  = zipCodes[zip]?.jsonObject ?: return null
        val stateCode = zipEntry["s"]?.jsonPrimitive?.content ?: return null
        val district  = zipEntry["d"]?.jsonPrimitive?.int ?: 0

        val stateEntry = officials[stateCode]?.jsonObject ?: return null
        val stateName  = stateEntry["name"]?.jsonPrimitive?.content ?: stateCode
        val governor   = stateEntry["governor"]?.jsonPrimitive?.content ?: ""
        val senator1   = stateEntry["senator1"]?.jsonPrimitive?.content ?: ""
        val senator2   = stateEntry["senator2"]?.jsonPrimitive?.content ?: ""
        val capital    = stateEntry["capital"]?.jsonPrimitive?.content ?: ""

        val districtKey = if (district == 0) "${stateCode}-AL" else "$stateCode-$district"
        val rep = districts[districtKey]?.jsonPrimitive?.content
            ?: districts["${stateCode}-1"]?.jsonPrimitive?.content
            ?: "Please verify at uscis.gov/citizenship/testupdates"

        return ZipLookupResult(
            stateCode      = stateCode,
            stateName      = stateName,
            governor       = governor,
            senator1       = senator1,
            senator2       = senator2,
            representative = rep,
            stateCapital   = capital,
            isExactMatch   = true
        )
    }

    /** Falls back to state-level officials using the first 3 digits of the zip. */
    private fun lookupByPrefix(zip: String): ZipLookupResult? {
        val stateCode  = zip3ToState[zip.take(3)] ?: return null
        val stateEntry = officials[stateCode]?.jsonObject ?: return null
        val stateName  = stateEntry["name"]?.jsonPrimitive?.content ?: stateCode
        val governor   = stateEntry["governor"]?.jsonPrimitive?.content ?: ""
        val senator1   = stateEntry["senator1"]?.jsonPrimitive?.content ?: ""
        val senator2   = stateEntry["senator2"]?.jsonPrimitive?.content ?: ""
        val capital    = stateEntry["capital"]?.jsonPrimitive?.content ?: ""

        return ZipLookupResult(
            stateCode      = stateCode,
            stateName      = stateName,
            governor       = governor,
            senator1       = senator1,
            senator2       = senator2,
            representative = "Visit house.gov/zip4 to look up your Representative",
            stateCapital   = capital,
            isExactMatch   = false
        )
    }

    /**
     * Maps every valid USPS 3-digit prefix to the corresponding state/territory code.
     * Derived from USPS Sectional Center Facility (SCF) assignments — stable across elections.
     */
    private val zip3ToState: Map<String, String> by lazy {
        buildMap {
            fun r(start: Int, end: Int, state: String) =
                (start..end).forEach { put(it.toString().padStart(3, '0'), state) }
            // Massachusetts
            r(10, 27, "MA")
            // Rhode Island
            r(28, 29, "RI")
            // New Hampshire
            r(30, 38, "NH")
            // Maine
            r(39, 49, "ME")
            // Vermont
            r(50, 59, "VT")
            // Connecticut
            r(60, 69, "CT")
            // New Jersey
            r(70, 89, "NJ")
            // New York
            r(100, 149, "NY")
            // Pennsylvania
            r(150, 196, "PA")
            // Delaware
            r(197, 199, "DE")
            // Washington DC
            r(200, 205, "DC")
            // Maryland (some 20x overlap with DC suburbs — prefer MD)
            r(206, 219, "MD")
            // Virginia
            r(220, 246, "VA")
            // West Virginia
            r(247, 268, "WV")
            // North Carolina
            r(270, 289, "NC")
            // South Carolina
            r(290, 299, "SC")
            // Georgia
            r(300, 319, "GA"); r(398, 399, "GA")
            // Florida
            r(320, 349, "FL")
            // Alabama
            r(350, 369, "AL")
            // Tennessee
            r(370, 385, "TN")
            // Mississippi
            r(386, 397, "MS")
            // Kentucky
            r(400, 418, "KY"); r(420, 427, "KY")
            // Ohio
            r(430, 459, "OH")
            // Indiana
            r(460, 479, "IN")
            // Michigan
            r(480, 499, "MI")
            // Iowa
            r(500, 528, "IA")
            // Wisconsin
            r(530, 549, "WI")
            // Minnesota
            r(550, 567, "MN")
            // South Dakota
            r(570, 577, "SD")
            // North Dakota
            r(580, 588, "ND")
            // Montana
            r(590, 599, "MT")
            // Illinois
            r(600, 629, "IL")
            // Missouri
            r(630, 658, "MO")
            // Kansas
            r(660, 679, "KS")
            // Nebraska
            r(680, 693, "NE")
            // Louisiana
            r(700, 714, "LA")
            // Arkansas
            r(716, 729, "AR")
            // Oklahoma
            r(730, 749, "OK")
            // Texas
            r(750, 799, "TX")
            // Colorado
            r(800, 816, "CO")
            // Wyoming
            r(820, 831, "WY")
            // Idaho
            r(832, 838, "ID")
            // Utah
            r(840, 847, "UT")
            // Arizona
            r(850, 865, "AZ")
            // New Mexico
            r(870, 884, "NM")
            // Nevada
            put("889", "NV"); r(890, 898, "NV")
            // California
            r(900, 961, "CA")
            // Hawaii
            r(967, 968, "HI")
            // Oregon
            r(970, 979, "OR")
            // Washington
            r(980, 994, "WA")
            // Alaska
            r(995, 999, "AK")
        }
    }
}
