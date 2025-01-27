import os

def create_code_template(language):
    templates = {
        'python': '''# Python Template
def main():
    print("Hello, World!")

if __name__ == "__main__":
    main()
''',
        'c++': '''// C++ Template
#include <iostream>
using namespace std;

int main() {
    cout << "Hello, World!" << endl;
    return 0;
}
''',
        'c': '''// C Template
#include <stdio.h>

int main() {
    printf("Hello, World!\\n");
    return 0;
}
''',
        'java': '''// Java Template
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
''',
        'c#': '''// C# Template
using System;

class Program {
    static void Main() {
        Console.WriteLine("Hello, World!");
    }
}
''',
        'react': '''// React Template (JavaScript)
import React from "react";

function App() {
  return (
    <div>
      <h1>Hello, World!</h1>
    </div>
  );
}

export default App;
''',
        'swift': '''// Swift Template
import Foundation

print("Hello, World!")
''',
        'php': '''<?php
// PHP Template
echo "Hello, World!";
?>
'''
    }

    return templates.get(language.lower(), "Language not supported")

def create_files():
    languages = ['python', 'c++', 'c', 'java', 'c#', 'react', 'swift', 'php']

    for language in languages:
        code = create_code_template(language)
        if code != "Language not supported":
            # Determine the file extension
            extension = {
                'python': '.py',
                'c++': '.cpp',
                'c': '.c',
                'java': '.java',
                'c#': '.cs',
                'react': '.js',
                'swift': '.swift',
                'php': '.php'
            }.get(language)

            # Create a filename based on the language
            filename = f"{language}_hello_world{extension}"

            # Create the file and write the template code
            with open(filename, 'w') as f:
                f.write(code)

            print(f"Created {filename}")

# Call the function to create the files
create_files()
