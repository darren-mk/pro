# tetris-engine-clj

Coding test assignment for the senior software engineer role at DRW.<br />
Written by Darren Kim.<br />

## Usage

### Run

To run the program on cli:<br />
`$ lein run <optional-input-file-name> <optional-output-file-name>`<br />

For example, you can enter with options:<br />
`$ lein run input.txt output.txt`<br />

In case you don't provide optional arguments,<br />
the program will try to read `input.txt` file saved in the project folder<br />
and generate `output.txt` file by default.<br />

So, you can simply run it by<br />
`$ lein run`<br />

Or, you can also run it on repl.<br />
`$ lein repl`<br />
`tetris-engine-clj.core=> (-main)`<br />

Or, in case you want to run on an excutable:<br />
`$ lein uberjar`<br />
`$ java -jar target/tetris-engine-clj-0.1.0-SNAPSHOT-standalone.jar`<br />

### Test

To run unit tests on cli:<br />
`$ lein test`<br />

## Expectation

With the data in `input.txt` file in the given problem,<br />
the program will print result below and save it in the output file.<br />
`2`<br />
`4`<br />
`0`<br />
`2`<br />
`4`<br />
`1`<br />
`0`<br />
`2`<br />
`2`<br />
`2`<br />
`1`<br />
`1`<br />
`4`<br />
`3`<br />
`1`<br />
`2`<br />
`1`<br />
`8`<br />
`8`<br />
`0`<br />
`3`<br />
