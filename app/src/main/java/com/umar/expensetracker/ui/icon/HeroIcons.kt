package com.umar.expensetracker.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/**
 * Heroicons v2 Outline style icons (24x24 viewport, 1.5dp stroke).
 */
object HeroIcons {
    private fun outline(name: String, pathData: String): ImageVector {
        return ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).addPath(
            pathData = PathParser().parsePathString(pathData).toNodes(),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ).build()
    }

    // Bell (Notifications)
    val Bell: ImageVector by lazy {
        outline(
            name = "Bell",
            pathData = "M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0"
        )
    }

    // Eye (Show balance)
    val Eye: ImageVector by lazy {
        outline(
            name = "Eye",
            pathData = "M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178ZM15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"
        )
    }

    // EyeSlash (Hide balance)
    val EyeSlash: ImageVector by lazy {
        outline(
            name = "EyeSlash",
            pathData = "M3.98 8.223A10.477 10.477 0 0 0 1.934 12C3.226 16.338 7.244 19.5 12 19.5c.993 0 1.953-.138 2.863-.395M6.228 6.228A10.451 10.451 0 0 1 12 4.5c4.756 0 8.773 3.162 10.065 7.498a10.522 10.522 0 0 1-4.293 5.774M6.228 6.228 3 3m3.228 3.228 3.65 3.65m7.894 7.894L21 21m-3.228-3.228-3.65-3.65m0 0a3 3 0 1 0-4.243-4.243m4.242 4.242L9.88 9.88"
        )
    }

    // ArrowTrendingUp (Financial growth)
    val ArrowTrendingUp: ImageVector by lazy {
        outline(
            name = "ArrowTrendingUp",
            pathData = "M2.25 18 9 11.25l4.306 4.306a11.95 11.95 0 0 1 5.814-5.518l2.74-1.22m0 0-5.94-2.281m5.94 2.28-2.28 5.941"
        )
    }

    // ArrowTrendingDown (Financial drop)
    val ArrowTrendingDown: ImageVector by lazy {
        outline(
            name = "ArrowTrendingDown",
            pathData = "M2.25 6 9 12.75l4.286-4.286a11.948 11.948 0 0 1 4.306 6.43l.776 2.898m0 0 3.182-5.511m-3.182 5.51-5.511-3.181"
        )
    }

    // ArrowDown (Income cashflow)
    val ArrowDown: ImageVector by lazy {
        outline(
            name = "ArrowDown",
            pathData = "M19.5 13.5 12 21m0 0-7.5-7.5M12 21V3"
        )
    }

    // ArrowUp (Spent cashflow)
    val ArrowUp: ImageVector by lazy {
        outline(
            name = "ArrowUp",
            pathData = "M4.5 10.5 12 3m0 0 7.5 7.5M12 3v18"
        )
    }

    // Plus (Add transaction)
    val Plus: ImageVector by lazy {
        outline(
            name = "Plus",
            pathData = "M12 4.5v15m7.5-7.5h-15"
        )
    }

    // PaperAirplane (Send action)
    val PaperAirplane: ImageVector by lazy {
        outline(
            name = "PaperAirplane",
            pathData = "M6 12 3.269 3.125A59.769 59.769 0 0 1 21.485 12 59.768 59.768 0 0 1 3.27 20.875L5.999 12Zm0 0h7.5"
        )
    }

    // ArrowsRightLeft (Transfer action)
    val ArrowsRightLeft: ImageVector by lazy {
        outline(
            name = "ArrowsRightLeft",
            pathData = "M7.5 21 3 16.5m0 0L7.5 12M3 16.5h13.5m0-13.5L21 7.5m0 0L16.5 12M21 7.5H7.5"
        )
    }

    // ChartBar (Insights / Analytics tab)
    val ChartBar: ImageVector by lazy {
        outline(
            name = "ChartBar",
            pathData = "M3 13.125C3 12.504 3.504 12 4.125 12h2.25c.621 0 1.125.504 1.125 1.125v6.75C7.5 20.496 6.996 21 6.375 21h-2.25A1.125 1.125 0 0 1 3 19.875v-6.75ZM9.75 8.625c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125v11.25c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 0 1-1.125-1.125V8.625ZM16.5 4.125c0-.621.504-1.125 1.125-1.125h2.25C20.496 3 21 3.504 21 4.125v15.75c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 0 1-1.125-1.125V4.125Z"
        )
    }

    // ChartPie (Breakdown / Budget overview)
    val ChartPie: ImageVector by lazy {
        outline(
            name = "ChartPie",
            pathData = "M10.5 6a7.5 7.5 0 1 0 7.5 7.5h-7.5V6Z"
        )
    }

    // ChevronRight (See all arrow)
    val ChevronRight: ImageVector by lazy {
        outline(
            name = "ChevronRight",
            pathData = "m8.25 4.5 7.5 7.5-7.5 7.5"
        )
    }

    // ChevronLeft (Back / Previous period)
    val ChevronLeft: ImageVector by lazy {
        outline(
            name = "ChevronLeft",
            pathData = "M15.75 19.5 8.25 12l7.5-7.5"
        )
    }

    // ChevronDown (Dropdown / Filter)
    val ChevronDown: ImageVector by lazy {
        outline(
            name = "ChevronDown",
            pathData = "m19.5 8.25-7.5 7.5-7.5-7.5"
        )
    }

    // ArrowPath (Refresh)
    val ArrowPath: ImageVector by lazy {
        outline(
            name = "ArrowPath",
            pathData = "M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0 3.181 3.183a8.25 8.25 0 0 0 13.803-3.7M4.031 9.865a8.25 8.25 0 0 1 13.803-3.7l3.181 3.182m0-4.991v4.99"
        )
    }

    // ExclamationCircle (Error / Alert)
    val ExclamationCircle: ImageVector by lazy {
        outline(
            name = "ExclamationCircle",
            pathData = "M12 9v3.75m9-.75a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-9 3.75h.008v.008H12v-.008Z"
        )
    }

    // MagnifyingGlass (Search transactions)
    val MagnifyingGlass: ImageVector by lazy {
        outline(
            name = "MagnifyingGlass",
            pathData = "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"
        )
    }

    // Funnel (Filter)
    val Funnel: ImageVector by lazy {
        outline(
            name = "Funnel",
            pathData = "M12 3c2.755 0 5.455.232 8.083.678.533.09.917.556.917 1.096v1.044a2.25 2.25 0 0 1-.659 1.591l-5.432 5.432a2.25 2.25 0 0 0-.659 1.591v2.927a2.25 2.25 0 0 1-1.244 2.013L9.75 21v-6.568a2.25 2.25 0 0 0-.659-1.591L3.659 7.409A2.25 2.25 0 0 1 3 5.818V4.774c0-.54.384-1.006.917-1.096A48.32 48.32 0 0 1 12 3Z"
        )
    }

    // CalendarDays (Date / Monthly filter)
    val CalendarDays: ImageVector by lazy {
        outline(
            name = "CalendarDays",
            pathData = "M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0v-7.5A2.25 2.25 0 0 1 5.25 9h13.5A2.25 2.25 0 0 1 21 11.25v7.5"
        )
    }

    // Wallet (Accounts / Wallets tab)
    val Wallet: ImageVector by lazy {
        outline(
            name = "Wallet",
            pathData = "M21 12a2.25 2.25 0 0 0-2.25-2.25H15a3 3 0 1 1-6 0H5.25A2.25 2.25 0 0 0 3 12m18 0v6a2.25 2.25 0 0 1-2.25 2.25H5.25A2.25 2.25 0 0 1 3 18v-6m18 0V9M3 12V9m18 0a2.25 2.25 0 0 0-2.25-2.25H5.25A2.25 2.25 0 0 0 3 9m18 0V6a2.25 2.25 0 0 0-2.25-2.25H5.25A2.25 2.25 0 0 0 3 6v3"
        )
    }

    // CreditCard (Cards / E-wallet / Bills)
    val CreditCard: ImageVector by lazy {
        outline(
            name = "CreditCard",
            pathData = "M2.25 8.25h19.5M2.25 9h19.5m-16.5 5.25h6m-6 2.25h3m-3.75 3h15a2.25 2.25 0 0 0 2.25-2.25V6.75A2.25 2.25 0 0 0 19.5 4.5h-15a2.25 2.25 0 0 0-2.25 2.25v10.5A2.25 2.25 0 0 0 4.5 19.5Z"
        )
    }

    // Cog6Tooth (Settings tab)
    val Cog6Tooth: ImageVector by lazy {
        outline(
            name = "Cog6Tooth",
            pathData = "M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.325.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 0 1 1.37.49l1.296 2.247a1.125 1.125 0 0 1-.26 1.431l-1.003.827c-.293.241-.438.613-.43.992a7.723 7.723 0 0 1 0 .255c-.008.378.137.75.43.991l1.004.827c.424.35.534.955.26 1.43l-1.298 2.247a1.125 1.125 0 0 1-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.47 6.47 0 0 1-.22.128c-.331.183-.581.495-.644.869l-.213 1.281c-.09.543-.56.94-1.11.94h-2.594c-.55 0-1.019-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 0 1-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 0 1-1.369-.49l-1.297-2.247a1.125 1.125 0 0 1 .26-1.431l1.004-.827c.292-.24.437-.613.43-.991a6.932 6.932 0 0 1 0-.255c.007-.38-.138-.751-.43-.992l-1.004-.827a1.125 1.125 0 0 1-.26-1.43l1.297-2.247a1.125 1.125 0 0 1 1.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.086.22-.128.332-.183.582-.495.644-.869l.214-1.28Z"
        )
    }

    // EllipsisVertical (Row / Card overflow menu)
    val EllipsisVertical: ImageVector by lazy {
        outline(
            name = "EllipsisVertical",
            pathData = "M12 6.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5ZM12 12.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5ZM12 18.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5Z"
        )
    }

    // EllipsisHorizontal (More options / More tab)
    val EllipsisHorizontal: ImageVector by lazy {
        outline(
            name = "EllipsisHorizontal",
            pathData = "M6.75 12a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0ZM12.75 12a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0ZM18.75 12a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0Z"
        )
    }

    // Tag (Category / Label)
    val Tag: ImageVector by lazy {
        outline(
            name = "Tag",
            pathData = "M9.568 3H5.25A2.25 2.25 0 0 0 3 5.25v4.318c0 .597.237 1.17.659 1.591l9.581 9.581c.699.699 1.78.872 2.607.33a18.095 18.095 0 0 0 5.223-5.223c.542-.827.369-1.908-.33-2.607L11.16 3.66A2.25 2.25 0 0 0 9.568 3Z"
        )
    }

    // ReceiptPercent (Discounts / Taxes / Bills)
    val ReceiptPercent: ImageVector by lazy {
        outline(
            name = "ReceiptPercent",
            pathData = "M9 14.25 15 8.25m-6 0h.008v.008H9V8.25Zm6 6h.008v.008H15v-.008ZM19.5 21l-1.5-1.5-1.5 1.5-1.5-1.5-1.5 1.5-1.5-1.5-1.5 1.5-1.5-1.5-1.5 1.5-1.5-1.5L3 21V3.75A.75.75 0 0 1 3.75 3h16.5a.75.75 0 0 1 .75.75V21Z"
        )
    }

    // Bolt (Electricity / Utilities / Quick action)
    val Bolt: ImageVector by lazy {
        outline(
            name = "Bolt",
            pathData = "m3.75 13.5 10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75Z"
        )
    }

    // Coffee / Cup (Cafe & Beverages)
    val CupHot: ImageVector by lazy {
        outline(
            name = "CupHot",
            pathData = "M4.5 9.75h12.75a.75.75 0 0 1 .75.75v3a5.25 5.25 0 0 1-5.25 5.25H9.75A5.25 5.25 0 0 1 4.5 13.5V9.75Zm13.5 0h1.5a2.25 2.25 0 0 1 0 4.5H18"
        )
    }

    // Cake (Food & Dining)
    val Cake: ImageVector by lazy {
        outline(
            name = "Cake",
            pathData = "M12 8.25v-1.5m0 1.5c-1.355 0-2.697.056-4.024.166C6.845 8.51 6 9.473 6 10.608v2.513m6-4.871c1.355 0 2.697.056 4.024.166C17.155 8.51 18 9.473 18 10.608v2.513M15 8.25v-1.5m-6 1.5v-1.5m12 9.75-1.5.75a3.354 3.354 0 0 1-3 0 3.354 3.354 0 0 0-3 0 3.354 3.354 0 0 1-3 0 3.354 3.354 0 0 0-3 0 3.354 3.354 0 0 1-3 0L3 16.5m15-3.379a48.474 48.474 0 0 0-6-.371c-2.032 0-4.034.125-6 .371v3.18c0 .597.237 1.17.659 1.591l.24.24c.42.42.993.659 1.59.659h7.022c.597 0 1.17-.238 1.591-.659l.24-.24a2.25 2.25 0 0 0 .658-1.592v-3.179Z"
        )
    }

    // ShoppingBag (Shopping)
    val ShoppingBag: ImageVector by lazy {
        outline(
            name = "ShoppingBag",
            pathData = "M15.75 10.5V6a3.75 3.75 0 1 0-7.5 0v4.5m11.356-1.993 1.263 12c.07.665-.45 1.243-1.119 1.243H4.25a1.125 1.125 0 0 1-1.12-1.243l1.264-12A1.125 1.125 0 0 1 5.513 7.5h12.974c.576 0 1.059.435 1.119 1.007ZM8.625 10.5a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm7.5 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z"
        )
    }

    // Home (Housing / Rent)
    val Home: ImageVector by lazy {
        outline(
            name = "Home",
            pathData = "m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"
        )
    }

    // Truck (Transport / Logistics)
    val Truck: ImageVector by lazy {
        outline(
            name = "Truck",
            pathData = "M8.25 18.75a1.5 1.5 0 0 1-3 0m3 0a1.5 1.5 0 0 0-3 0m3 0h6m-9 0H3.375a1.125 1.125 0 0 1-1.125-1.125V14.25m17.25 4.5a1.5 1.5 0 0 1-3 0m3 0a1.5 1.5 0 0 0-3 0m3 0h1.125c.621 0 1.129-.504 1.09-1.124a17.902 17.902 0 0 0-3.213-9.193 2.056 2.056 0 0 0-1.58-.86H14.25M16.5 18.75h-2.25m0-11.175V3.375c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v10.875"
        )
    }

    // Film (Entertainment / Subscriptions)
    val Film: ImageVector by lazy {
        outline(
            name = "Film",
            pathData = "m15.75 10.5 4.72-4.72a.75.75 0 0 1 1.28.53v11.38a.75.75 0 0 1-1.28.53l-4.72-4.72M4.5 18.75h9a2.25 2.25 0 0 0 2.25-2.25v-9a2.25 2.25 0 0 0-2.25-2.25h-9A2.25 2.25 0 0 0 2.25 7.5v9a2.25 2.25 0 0 0 2.25 2.25Z"
        )
    }

    // Heart (Health & Medical)
    val Heart: ImageVector by lazy {
        outline(
            name = "Heart",
            pathData = "M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
        )
    }

    // Banknotes (Salary / Cash)
    val Banknotes: ImageVector by lazy {
        outline(
            name = "Banknotes",
            pathData = "M2.25 18.75a60.07 60.07 0 0 1 15.797 2.101c.727.198 1.453-.342 1.453-1.096V18.75M3.75 4.5v.75A.75.75 0 0 1 3 6H2.25m0 0v8.25m0 0a60.07 60.07 0 0 1 15.797 2.101c.727.198 1.453-.342 1.453-1.096V14.25M3.75 4.5v.75A.75.75 0 0 1 3 6H2.25m15 0h2.25a.75.75 0 0 1 .75.75v.75m0 0A60.07 60.07 0 0 0 5.203 5.399C4.476 5.201 3.75 5.741 3.75 6.495V7.5M18 14.25v-.75a.75.75 0 0 1 .75-.75h2.25m0 0V5.25m0 0a60.07 60.07 0 0 0-15.797-2.101C4.476 2.951 3.75 3.491 3.75 4.245V5.25m15 9V14.25"
        )
    }

    // CurrencyDollar (Investment / Savings)
    val CurrencyDollar: ImageVector by lazy {
        outline(
            name = "CurrencyDollar",
            pathData = "M12 6v12m-3-2.818.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"
        )
    }

    // DocumentText (Receipts / Statements / Notes)
    val DocumentText: ImageVector by lazy {
        outline(
            name = "DocumentText",
            pathData = "M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m2.25 0H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z"
        )
    }
}