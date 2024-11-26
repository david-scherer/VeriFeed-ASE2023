

def read_html_page(file_path, n_first_lines_to_skip: int = 3, n_last_lines_to_skip: int = 2):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    if len(lines) > 1:
        modified_lines = lines[n_first_lines_to_skip:-n_last_lines_to_skip]
    else:
        modified_lines = lines

    return "\n".join(modified_lines)


def compose_page():
    prefix = "<div class=\"grid-item\">"
    suffix = "</div>"

    barchart = prefix + read_html_page("barchart.html") + suffix
    heatmap = prefix + read_html_page("heatmap.html") + suffix
    term_rank = prefix + read_html_page("term_rank.html") + suffix
    topics = prefix + read_html_page("topics.html") + suffix

    content = '''<!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>VeriFeed - Topic Model Dashboard</title>
            <style>
            
                body, html {
                    margin: 0;
                    padding: 0;
                    height: 100%;
                    background-color: #f4f4f4;
                }
            
                .grid-container {
                    display: grid;
                    grid-template-columns: repeat(2, 1fr); /* 2 columns with equal width */
                    grid-gap: 10px; /* Adjust the gap between grid items */
                    padding: 10px;
                }
        
                .grid-item {
                    border: 1px solid #ccc;
                    padding: 20px;
                    text-align: center;
                    background-color: #fff; /* Background color for better visibility */
                    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                }
                
                .main-title {
                    font-family: 'Roboto', sans-serif;
                    text-align: center;
                    color: #333;
                    margin-top: 20px;
                }
            </style>
        </head>
        <body>
            <h1 class="main-title"> VeriFeed - Topic Modeling Dashboard </h1>
            <!-- Grid container -->
            <div class="grid-container">
        
        ''' + topics + term_rank + barchart + heatmap + '''
        </div>
        
        </body>
        </html>
        '''

    return content
