from n2t.core.compiler.compilation_engine import CompilationEngine


class JackAnalyzer:
    def __init__(self, input_file: str) -> None:
        self.input_file = input_file

    def analyze(self) -> None:
        output_file = self.input_file.replace(".jack", ".xml")
        compiler = CompilationEngine(self.input_file, output_file)
        compiler.compile_class()
