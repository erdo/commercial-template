package foo.bar.clean.domain.services.csv

import java.util.StringTokenizer

class CsvParser(
    csvData: String,
    delimiter: String = CSV_DEFAULT_DELIMITER
) : Iterable<CsvParser.Line> {

    private val lines: MutableList<Line> = ArrayList()

    init {
        require(delimiter.isNotBlank()) {
            throw RuntimeException("delimiter needs to be at least 1 non-whitespace character")
        }
        parseLines(csvData, delimiter)
    }

    operator fun get(index: Int): Line = lines[index]

    override fun iterator(): MutableIterator<Line> {
        return lines.iterator()
    }

    private fun parseLines(csvData: String, delimiter: String) {
        val stringTokenizer = StringTokenizer(csvData, CSV_LINE_TERMINATOR, false)
        while (stringTokenizer.hasMoreTokens()) {
            lines.add(Line(stringTokenizer.nextToken(), delimiter))
        }
    }

    class Line(
        line: String,
        delimiter: String
    ) : Iterable<String> {

        private val columns: MutableList<String> = ArrayList()

        init {
            val stringTokenizer = StringTokenizer(line, delimiter, false)
            while (stringTokenizer.hasMoreTokens()) {
                columns.add(stringTokenizer.nextToken());
            }
        }

        operator fun get(index: Int): String = columns[index]

        override fun iterator(): MutableIterator<String> {
            return columns.iterator()
        }
    }
}
